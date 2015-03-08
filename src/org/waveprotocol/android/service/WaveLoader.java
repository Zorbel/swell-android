package org.waveprotocol.android.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import org.waveprotocol.wave.client.OptimalGroupingScheduler;
import org.waveprotocol.wave.client.common.util.ClientPercentEncoderDecoder;
import org.waveprotocol.wave.client.concurrencycontrol.LiveChannelBinder;
import org.waveprotocol.wave.client.concurrencycontrol.MuxConnector;
import org.waveprotocol.wave.client.concurrencycontrol.WaveletOperationalizer;
import org.waveprotocol.wave.client.editor.content.Registries;
import org.waveprotocol.wave.client.scheduler.SchedulerInstance;
import org.waveprotocol.wave.client.wave.LazyContentDocument;
import org.waveprotocol.wave.client.wave.LocalSupplementedWave;
import org.waveprotocol.wave.client.wave.LocalSupplementedWaveImpl;
import org.waveprotocol.wave.client.wave.RegistriesHolder;
import org.waveprotocol.wave.client.wave.SimpleDiffDoc;
import org.waveprotocol.wave.client.wave.WaveDocuments;
import org.waveprotocol.wave.common.logging.LoggerBundle;
import org.waveprotocol.wave.concurrencycontrol.channel.OperationChannelMultiplexer;
import org.waveprotocol.wave.concurrencycontrol.channel.OperationChannelMultiplexerImpl;
import org.waveprotocol.wave.concurrencycontrol.channel.OperationChannelMultiplexerImpl.LoggerContext;
import org.waveprotocol.wave.concurrencycontrol.channel.ViewChannelFactory;
import org.waveprotocol.wave.concurrencycontrol.channel.ViewChannelImpl;
import org.waveprotocol.wave.concurrencycontrol.channel.WaveViewService;
import org.waveprotocol.wave.concurrencycontrol.common.UnsavedDataListener;
import org.waveprotocol.wave.concurrencycontrol.common.UnsavedDataListenerFactory;
import org.waveprotocol.wave.model.conversation.ObservableConversationView;
import org.waveprotocol.wave.model.conversation.WaveBasedConversationView;
import org.waveprotocol.wave.model.document.WaveContext;
import org.waveprotocol.wave.model.document.indexed.IndexedDocumentImpl;
import org.waveprotocol.wave.model.document.operation.DocInitialization;
import org.waveprotocol.wave.model.extended.WaveExtendedModel;
import org.waveprotocol.wave.model.id.IdConstants;
import org.waveprotocol.wave.model.id.IdFilter;
import org.waveprotocol.wave.model.id.IdGenerator;
import org.waveprotocol.wave.model.id.IdURIEncoderDecoder;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.id.WaveletId;
import org.waveprotocol.wave.model.schema.SchemaProvider;
import org.waveprotocol.wave.model.schema.conversation.ConversationSchemas;
import org.waveprotocol.wave.model.supplement.LiveSupplementedWaveImpl;
import org.waveprotocol.wave.model.supplement.ObservablePrimitiveSupplement;
import org.waveprotocol.wave.model.supplement.ObservableSupplementedWave;
import org.waveprotocol.wave.model.supplement.SupplementedWaveImpl.DefaultFollow;
import org.waveprotocol.wave.model.supplement.WaveletBasedSupplement;
import org.waveprotocol.wave.model.util.FuzzingBackOffScheduler;
import org.waveprotocol.wave.model.util.FuzzingBackOffScheduler.CollectiveScheduler;
import org.waveprotocol.wave.model.util.Scheduler;
import org.waveprotocol.wave.model.version.HashedVersion;
import org.waveprotocol.wave.model.version.HashedVersionFactory;
import org.waveprotocol.wave.model.version.HashedVersionZeroFactoryImpl;
import org.waveprotocol.wave.model.wave.ParticipantId;
import org.waveprotocol.wave.model.wave.Wavelet;
import org.waveprotocol.wave.model.wave.data.DocumentFactory;
import org.waveprotocol.wave.model.wave.data.ObservableWaveletData;
import org.waveprotocol.wave.model.wave.data.WaveViewData;
import org.waveprotocol.wave.model.wave.data.impl.ObservablePluggableMutableDocument;
import org.waveprotocol.wave.model.wave.data.impl.WaveViewDataImpl;
import org.waveprotocol.wave.model.wave.data.impl.WaveletDataImpl;
import org.waveprotocol.wave.model.wave.opbased.OpBasedWavelet;
import org.waveprotocol.wave.model.wave.opbased.WaveViewImpl;
import org.waveprotocol.wave.model.wave.opbased.WaveViewImpl.WaveletConfigurator;
import org.waveprotocol.wave.model.wave.opbased.WaveViewImpl.WaveletFactory;
import org.waveprotocol.wave.model.waveref.WaveRef;

import com.google.common.base.Preconditions;
import com.google.gwt.user.client.Command;

public class WaveLoader {

  private boolean isClosed = true;

  // Provided objects

  private final WaveRef waveRef;
  private final boolean isNewWave;
  private final Set<ParticipantId> otherParticipants;
  private IdGenerator idGenerator;
  private ParticipantId signedInuser;

  private final RemoteViewServiceMultiplexer channel;
  private final UnsavedDataListener unsavedDataListener;

  private WaveContext waveContext;


  private CollectiveScheduler rpcScheduler;

  // Wave stack.
  private WaveViewData waveData;

  private WaveDocuments<LazyContentDocument> documentRegistry;
  private WaveletOperationalizer wavelets;
  private WaveViewImpl<OpBasedWavelet> wave;
  private MuxConnector connector;

  // Model objects
  private ObservableConversationView conversations;
  private LocalSupplementedWave supplement;


  public static WaveLoader create(boolean isNewWave, WaveRef waveRef,
      RemoteViewServiceMultiplexer channel, ParticipantId participant,
      Set<ParticipantId> otherParticipants,
      IdGenerator idGenerator,
      UnsavedDataListener unsavedDataListener) {

    WaveLoader loader = new WaveLoader(isNewWave, waveRef, channel, participant, otherParticipants,
        idGenerator,
        unsavedDataListener);
    loader.init();
    return loader;
  }


  protected WaveLoader(boolean isNewWave, WaveRef waveRef, RemoteViewServiceMultiplexer channel,
      ParticipantId participant,
      Set<ParticipantId> otherParticipants, IdGenerator idGenerator,
      UnsavedDataListener unsavedDataListener) {
    this.signedInuser = participant;
    this.waveRef = waveRef;
    this.isNewWave = isNewWave;
    this.idGenerator = idGenerator;
    this.channel = channel;
    this.otherParticipants = otherParticipants;
    this.unsavedDataListener = unsavedDataListener;
  }

  protected void init() {

    waveData = WaveViewDataImpl.create(waveRef.getWaveId());

    if (isNewWave) {

      // Code taken from StageTwoProvider

      // For a new wave, initial state comes from local initialization.
      // getConversations().createRoot().getRootThread().appendBlip();

      // Adding any initial participant to the new wave
      // getConversations().getRoot().addParticipantIds(otherParticipants);

      // getConversations().createRoot().addParticipantIds(otherParticipants);

      // Install diff control before rendering, because logical diff state may
      // need to be adjusted due to arbitrary UI policies.

      getConversations().createRoot().addParticipantIds(otherParticipants);

      getConnector().connect(null);



    } else {

      // For an existing wave, while we're still using the old protocol,
      // rendering must be delayed until the channel is opened, because the
      // initial state snapshots come from the channel.


      // Install diff control before rendering, because logical diff state may
      // need to be adjusted due to arbitrary UI policies.
      getConnector().connect(null);
    }

    isClosed = false;
  }

  public WaveContext getWaveContext() {
    if (isClosed)
      return null;

    if (waveContext == null)
      waveContext = new WaveContext(getWave(), getConversations(), getSupplement(), null);

    return waveContext;
  }

  public void close() {
    if (!isClosed) {
      getConnector().close();
      isClosed = true;
    }
  }

  //
  // Level 1
  //

  protected WaveViewData getWaveData() {
    Preconditions.checkState(waveData != null, "wave not ready");
    return waveData;
  }

  protected ObservableConversationView getConversations() {
    return conversations == null ? conversations = createConversations() : conversations;
  }

  protected ObservableConversationView createConversations() {
    return WaveBasedConversationView.create(getWave(), getIdGenerator());
  }

  protected MuxConnector getConnector() {
    return connector == null ? connector = createConnector() : connector;
  }

  protected MuxConnector createConnector() {
    LoggerBundle logger = LoggerBundle.NOP_IMPL;
    LoggerContext loggers = new LoggerContext(logger, logger, logger, logger);

    IdURIEncoderDecoder uriCodec = new IdURIEncoderDecoder(new ClientPercentEncoderDecoder());
    HashedVersionFactory hashFactory = new HashedVersionZeroFactoryImpl(uriCodec);

    Scheduler scheduler = new FuzzingBackOffScheduler.Builder(getRpcScheduler()).build();

    ViewChannelFactory viewFactory = ViewChannelImpl.factory(createWaveViewService(), logger);
    UnsavedDataListenerFactory unsyncedListeners = new UnsavedDataListenerFactory() {

      private final UnsavedDataListener listener = unsavedDataListener;

      @Override
      public UnsavedDataListener create(WaveletId waveletId) {
        return listener;
      }

      @Override
      public void destroy(WaveletId waveletId) {
      }
    };

    WaveletId udwId = getIdGenerator().newUserDataWaveletId(getSignedInUser().getAddress());

    ArrayList<String> prefixes = new ArrayList<String>();
    prefixes.add(IdConstants.CONVERSATION_WAVELET_PREFIX);
    prefixes.add(WaveExtendedModel.CONTENT_WAVELET_CHAT_PREFIX);

    final IdFilter filter = IdFilter.of(Collections.singleton(udwId), prefixes);

    WaveletDataImpl.Factory snapshotFactory = WaveletDataImpl.Factory.create(getDocumentRegistry());
    final OperationChannelMultiplexer mux = new OperationChannelMultiplexerImpl(getWave()
        .getWaveId(), viewFactory, snapshotFactory, loggers, unsyncedListeners, scheduler,
        hashFactory);

    final WaveViewImpl<OpBasedWavelet> wave = getWave();

    return new MuxConnector() {
      @Override
      public void connect(Command onOpened) {
        LiveChannelBinder.openAndBind(getWavelets(), wave, getDocumentRegistry(), mux, filter,
            onOpened);
      }

      @Override
      public void close() {
        mux.close();
      }
    };
  }

  //
  // Level 2
  //

  protected ParticipantId getSignedInUser() {
    return signedInuser;
  }

  protected IdGenerator getIdGenerator() {
    return idGenerator; // TODO assess whether create the id generator here
  }

  protected WaveViewImpl<OpBasedWavelet> getWave() {
    return wave == null ? wave = createWave() : wave;
  }

  protected WaveViewImpl<OpBasedWavelet> createWave() {
    WaveViewData snapshot = getWaveData();
    // The operationalizer makes the wavelets function via operation control.
    // The hookup with concurrency-control and remote operation streams occurs
    // later in createUpgrader().
    final WaveletOperationalizer operationalizer = getWavelets();
    WaveletFactory<OpBasedWavelet> waveletFactory = new WaveletFactory<OpBasedWavelet>() {
      @Override
      public OpBasedWavelet create(WaveId waveId, WaveletId id, ParticipantId creator) {
        long now = System.currentTimeMillis();
        ObservableWaveletData data = new WaveletDataImpl(id, creator, now, 0L,
            HashedVersion.unsigned(0), now, waveId, getDocumentRegistry());
        return operationalizer.operationalize(data);
      }
    };
    WaveViewImpl<OpBasedWavelet> wave = WaveViewImpl.create(waveletFactory, snapshot.getWaveId(),
        getIdGenerator(), getSignedInUser(), WaveletConfigurator.ADD_CREATOR);

    // Populate the initial state.
    for (ObservableWaveletData waveletData : snapshot.getWavelets()) {
      wave.addWavelet(operationalizer.operationalize(waveletData));
    }
    return wave;
  }

  protected CollectiveScheduler getRpcScheduler() {
    return rpcScheduler == null ? rpcScheduler = createRpcScheduler() : rpcScheduler;
  }

  protected CollectiveScheduler createRpcScheduler() {
    // Use a scheduler that runs closely-timed tasks at the same time.
    return new OptimalGroupingScheduler(SchedulerInstance.getLowPriorityTimer());
  }

  protected WaveViewService createWaveViewService() {
    return new RemoteWaveViewService(waveRef.getWaveId(), channel, getDocumentRegistry());
  }


  protected WaveDocuments<LazyContentDocument> getDocumentRegistry() {
    return documentRegistry == null
        ? documentRegistry = createDocumentRegistry() : documentRegistry;
  }

  protected WaveDocuments<LazyContentDocument> createDocumentRegistry() {
    IndexedDocumentImpl.performValidation = false;

    DocumentFactory<?> dataDocFactory =
        ObservablePluggableMutableDocument.createFactory(createSchemas());
    DocumentFactory<LazyContentDocument> blipDocFactory =
        new DocumentFactory<LazyContentDocument>() {
          private final Registries registries = RegistriesHolder.get();

          @Override
          public LazyContentDocument create(
              WaveletId waveletId, String docId, DocInitialization content) {
            // TODO(piotrkaleta,hearnden): hook up real diff state.
            SimpleDiffDoc noDiff = SimpleDiffDoc.create(content, null);
            return LazyContentDocument.create(registries, noDiff);
          }
        };

    return WaveDocuments.create(blipDocFactory, dataDocFactory);
  }

  protected WaveletOperationalizer getWavelets() {
    return wavelets == null ? wavelets = createWavelets() : wavelets;
  }

  protected WaveletOperationalizer createWavelets() {
    return WaveletOperationalizer.create(getWaveData().getWaveId(), getSignedInUser());
  }

  //
  // Level 3
  //

  protected SchemaProvider createSchemas() {
    return new ConversationSchemas();
  }

  public final LocalSupplementedWave getSupplement() {
    return null;
    // Supplement relies on GWT scheduler, so we ignore it
    // anyway
    // return supplement == null ? supplement = createSupplement() : supplement;
  }

  protected LocalSupplementedWave createSupplement() {
    Wavelet udw = getWave().getUserData();
    if (udw == null) {
      udw = getWave().createUserData();
    }
    ObservablePrimitiveSupplement state = WaveletBasedSupplement.create(udw);
    ObservableSupplementedWave live = new LiveSupplementedWaveImpl(state, getWave(),
        getSignedInUser(), DefaultFollow.ALWAYS, getConversations());
    return LocalSupplementedWaveImpl.create(getWave(), live);
  }

}
