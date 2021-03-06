/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.waveprotocol.wave.diff.jso;

import static org.waveprotocol.wave.communication.gwt.JsonHelper.*;
import com.google.gwt.core.client.*;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import org.waveprotocol.wave.federation.ProtocolDocumentOperation;
import org.waveprotocol.wave.federation.jso.ProtocolDocumentOperationJsoImpl;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import org.waveprotocol.wave.diff.DocumentDiffSnapshot;
import org.waveprotocol.wave.diff.DocumentDiffSnapshotUtil;
import org.waveprotocol.wave.communication.Blob;
import org.waveprotocol.wave.communication.ProtoEnums;
import org.waveprotocol.wave.communication.gwt.*;
import org.waveprotocol.wave.communication.json.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Client implementation of DocumentDiffSnapshot backed by a GWT JavaScriptObject.
 *
 * Generated from org/waveprotocol/wave/diff/diff.proto. Do not edit.
 */

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/** We have to use fully-qualified name of the GsonSerializable class here in order to make it
 * visible in case of nested classes.
 */
public final class DocumentDiffSnapshotJsoImpl extends org.waveprotocol.wave.communication.gwt.JsonMessage
    implements DocumentDiffSnapshot {
  private static final String keyDocumentId = "1";
  private static final String keyState = "2";
  private static final String keyDiff = "21";
  private static final String keyAuthor = "3";
  private static final String keyContributor = "4";
  private static final String keyAddedContributor = "22";
  private static final String keyRemovedContributor = "23";
  private static final String keyLastModifiedVersion = "5";
  private static final String keyLastModifiedTime = "6";
  protected DocumentDiffSnapshotJsoImpl() {
  }

  public static DocumentDiffSnapshotJsoImpl create() {
    DocumentDiffSnapshotJsoImpl instance = (DocumentDiffSnapshotJsoImpl) JsonMessage.createJsonMessage();
    // Force all lists to start with an empty list rather than no property for
    // the list. This is so that the native JS equality works, since (obviously)
    // {} != {"foo": []} while in the context of messages they should be.
    instance.clearContributor();
    instance.clearAddedContributor();
    instance.clearRemovedContributor();
    return instance;
  }

  @Override
  public void copyFrom(DocumentDiffSnapshot message) {
    super.copyFrom((DocumentDiffSnapshotJsoImpl) message);
  }

  /**
   * Licensed to the Apache Software Foundation (ASF) under one
   * or more contributor license agreements. See the NOTICE file
   * distributed with this work for additional information
   * regarding copyright ownership. The ASF licenses this file
   * to you under the Apache License, Version 2.0 (the
   * "License"); you may not use this file except in compliance
   * with the License. You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing,
   * software distributed under the License is distributed on an
   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   * KIND, either express or implied. See the License for the
   * specific language governing permissions and limitations
   * under the License.
   */

  @Override
  public String getDocumentId() {
    return hasProperty(this, keyDocumentId) ? getPropertyAsString(this, keyDocumentId) : null;
  }

  @Override
  public void setDocumentId(String value) {
    setPropertyAsString(this, keyDocumentId, value);
  }

  /**
   * Licensed to the Apache Software Foundation (ASF) under one
   * or more contributor license agreements. See the NOTICE file
   * distributed with this work for additional information
   * regarding copyright ownership. The ASF licenses this file
   * to you under the Apache License, Version 2.0 (the
   * "License"); you may not use this file except in compliance
   * with the License. You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing,
   * software distributed under the License is distributed on an
   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   * KIND, either express or implied. See the License for the
   * specific language governing permissions and limitations
   * under the License.
   */

  @Override
  public boolean hasState() {
    return hasProperty(this, keyState);
  }

  @Override
  public void clearState() {
    if (hasProperty(this, keyState)) {
      deleteProperty(this, keyState);
    }
  }

  @Override
  public ProtocolDocumentOperation getState() {
    return hasProperty(this, keyState) ? ((ProtocolDocumentOperationJsoImpl) getPropertyAsObject(this, keyState)) : null;
  }

  @Override
  public void setState(ProtocolDocumentOperation model) {
    setPropertyAsObject(this, keyState, (ProtocolDocumentOperationJsoImpl) model);
  }

  /**
   * Licensed to the Apache Software Foundation (ASF) under one
   * or more contributor license agreements. See the NOTICE file
   * distributed with this work for additional information
   * regarding copyright ownership. The ASF licenses this file
   * to you under the Apache License, Version 2.0 (the
   * "License"); you may not use this file except in compliance
   * with the License. You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing,
   * software distributed under the License is distributed on an
   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   * KIND, either express or implied. See the License for the
   * specific language governing permissions and limitations
   * under the License.
   */

  @Override
  public boolean hasDiff() {
    return hasProperty(this, keyDiff);
  }

  @Override
  public void clearDiff() {
    if (hasProperty(this, keyDiff)) {
      deleteProperty(this, keyDiff);
    }
  }

  @Override
  public ProtocolDocumentOperation getDiff() {
    return hasProperty(this, keyDiff) ? ((ProtocolDocumentOperationJsoImpl) getPropertyAsObject(this, keyDiff)) : null;
  }

  @Override
  public void setDiff(ProtocolDocumentOperation model) {
    setPropertyAsObject(this, keyDiff, (ProtocolDocumentOperationJsoImpl) model);
  }

  /**
   * Licensed to the Apache Software Foundation (ASF) under one
   * or more contributor license agreements. See the NOTICE file
   * distributed with this work for additional information
   * regarding copyright ownership. The ASF licenses this file
   * to you under the Apache License, Version 2.0 (the
   * "License"); you may not use this file except in compliance
   * with the License. You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing,
   * software distributed under the License is distributed on an
   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   * KIND, either express or implied. See the License for the
   * specific language governing permissions and limitations
   * under the License.
   */

  @Override
  public String getAuthor() {
    return hasProperty(this, keyAuthor) ? getPropertyAsString(this, keyAuthor) : null;
  }

  @Override
  public void setAuthor(String value) {
    setPropertyAsString(this, keyAuthor, value);
  }

  /**
   * Licensed to the Apache Software Foundation (ASF) under one
   * or more contributor license agreements. See the NOTICE file
   * distributed with this work for additional information
   * regarding copyright ownership. The ASF licenses this file
   * to you under the Apache License, Version 2.0 (the
   * "License"); you may not use this file except in compliance
   * with the License. You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing,
   * software distributed under the License is distributed on an
   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   * KIND, either express or implied. See the License for the
   * specific language governing permissions and limitations
   * under the License.
   */

  @Override
  public List<String> getContributor() {
    initArray(this, keyContributor);
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < getContributorSize(); i++) {
      list.add(getContributor(i));
    }
    return list;
  }

  @Override
  public void addAllContributor(List<String> values) {
    for (String value : values) {
      addContributor(value);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
      public String getContributor(int n) {
    initArray(this, keyContributor);
    JsArrayString array = getPropertyAsObject(this, keyContributor).cast();
    if (n < 0) throw new IllegalArgumentException("index " + n + " < 0");
    if (array.length() <= n) throw new IllegalArgumentException("index " + n + ">= array length " + array.length());
    return array.get(n);
  }

  @Override
  public void setContributor(int n, String value) {
    if (n < 0) throw new IllegalArgumentException("index " + n + " < 0");
    initArray(this, keyContributor);
    ((JsArrayString) getPropertyAsObject(this, keyContributor)).set(n, value);
  }

  @Override
  public int getContributorSize() {
    return hasProperty(this, keyContributor) ? ((JsArray<?>) getPropertyAsObject(this, keyContributor)).length() : 0;
  }

  @Override
  public void addContributor(String value) {
    initArray(this, keyContributor);
    ((JsArrayString) getPropertyAsObject(this, keyContributor)).push(value);
  }

  @Override
  public void clearContributor() {
    clearArray(this, keyContributor);
  }

  /**
   * Licensed to the Apache Software Foundation (ASF) under one
   * or more contributor license agreements. See the NOTICE file
   * distributed with this work for additional information
   * regarding copyright ownership. The ASF licenses this file
   * to you under the Apache License, Version 2.0 (the
   * "License"); you may not use this file except in compliance
   * with the License. You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing,
   * software distributed under the License is distributed on an
   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   * KIND, either express or implied. See the License for the
   * specific language governing permissions and limitations
   * under the License.
   */

  @Override
  public List<String> getAddedContributor() {
    initArray(this, keyAddedContributor);
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < getAddedContributorSize(); i++) {
      list.add(getAddedContributor(i));
    }
    return list;
  }

  @Override
  public void addAllAddedContributor(List<String> values) {
    for (String value : values) {
      addAddedContributor(value);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
      public String getAddedContributor(int n) {
    initArray(this, keyAddedContributor);
    JsArrayString array = getPropertyAsObject(this, keyAddedContributor).cast();
    if (n < 0) throw new IllegalArgumentException("index " + n + " < 0");
    if (array.length() <= n) throw new IllegalArgumentException("index " + n + ">= array length " + array.length());
    return array.get(n);
  }

  @Override
  public void setAddedContributor(int n, String value) {
    if (n < 0) throw new IllegalArgumentException("index " + n + " < 0");
    initArray(this, keyAddedContributor);
    ((JsArrayString) getPropertyAsObject(this, keyAddedContributor)).set(n, value);
  }

  @Override
  public int getAddedContributorSize() {
    return hasProperty(this, keyAddedContributor) ? ((JsArray<?>) getPropertyAsObject(this, keyAddedContributor)).length() : 0;
  }

  @Override
  public void addAddedContributor(String value) {
    initArray(this, keyAddedContributor);
    ((JsArrayString) getPropertyAsObject(this, keyAddedContributor)).push(value);
  }

  @Override
  public void clearAddedContributor() {
    clearArray(this, keyAddedContributor);
  }

  /**
   * Licensed to the Apache Software Foundation (ASF) under one
   * or more contributor license agreements. See the NOTICE file
   * distributed with this work for additional information
   * regarding copyright ownership. The ASF licenses this file
   * to you under the Apache License, Version 2.0 (the
   * "License"); you may not use this file except in compliance
   * with the License. You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing,
   * software distributed under the License is distributed on an
   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   * KIND, either express or implied. See the License for the
   * specific language governing permissions and limitations
   * under the License.
   */

  @Override
  public List<String> getRemovedContributor() {
    initArray(this, keyRemovedContributor);
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < getRemovedContributorSize(); i++) {
      list.add(getRemovedContributor(i));
    }
    return list;
  }

  @Override
  public void addAllRemovedContributor(List<String> values) {
    for (String value : values) {
      addRemovedContributor(value);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
      public String getRemovedContributor(int n) {
    initArray(this, keyRemovedContributor);
    JsArrayString array = getPropertyAsObject(this, keyRemovedContributor).cast();
    if (n < 0) throw new IllegalArgumentException("index " + n + " < 0");
    if (array.length() <= n) throw new IllegalArgumentException("index " + n + ">= array length " + array.length());
    return array.get(n);
  }

  @Override
  public void setRemovedContributor(int n, String value) {
    if (n < 0) throw new IllegalArgumentException("index " + n + " < 0");
    initArray(this, keyRemovedContributor);
    ((JsArrayString) getPropertyAsObject(this, keyRemovedContributor)).set(n, value);
  }

  @Override
  public int getRemovedContributorSize() {
    return hasProperty(this, keyRemovedContributor) ? ((JsArray<?>) getPropertyAsObject(this, keyRemovedContributor)).length() : 0;
  }

  @Override
  public void addRemovedContributor(String value) {
    initArray(this, keyRemovedContributor);
    ((JsArrayString) getPropertyAsObject(this, keyRemovedContributor)).push(value);
  }

  @Override
  public void clearRemovedContributor() {
    clearArray(this, keyRemovedContributor);
  }

  /**
   * Licensed to the Apache Software Foundation (ASF) under one
   * or more contributor license agreements. See the NOTICE file
   * distributed with this work for additional information
   * regarding copyright ownership. The ASF licenses this file
   * to you under the Apache License, Version 2.0 (the
   * "License"); you may not use this file except in compliance
   * with the License. You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing,
   * software distributed under the License is distributed on an
   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   * KIND, either express or implied. See the License for the
   * specific language governing permissions and limitations
   * under the License.
   */

  @Override
  public double getLastModifiedVersion() {
    return hasProperty(this, keyLastModifiedVersion) ? getPropertyAsDouble(this, keyLastModifiedVersion) : 0L;
  }

  @Override
  public void setLastModifiedVersion(double value) {
    setPropertyAsDouble(this, keyLastModifiedVersion, value);
  }

  /**
   * Licensed to the Apache Software Foundation (ASF) under one
   * or more contributor license agreements. See the NOTICE file
   * distributed with this work for additional information
   * regarding copyright ownership. The ASF licenses this file
   * to you under the Apache License, Version 2.0 (the
   * "License"); you may not use this file except in compliance
   * with the License. You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing,
   * software distributed under the License is distributed on an
   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   * KIND, either express or implied. See the License for the
   * specific language governing permissions and limitations
   * under the License.
   */

  @Override
  public double getLastModifiedTime() {
    return hasProperty(this, keyLastModifiedTime) ? getPropertyAsDouble(this, keyLastModifiedTime) : 0L;
  }

  @Override
  public void setLastModifiedTime(double value) {
    setPropertyAsDouble(this, keyLastModifiedTime, value);
  }

  @Override
  public boolean isEqualTo(Object o) {
    if (o instanceof DocumentDiffSnapshotJsoImpl) {
      return nativeIsEqualTo(o);
    } else if (o instanceof DocumentDiffSnapshot) {
      return DocumentDiffSnapshotUtil.isEqual(this, (DocumentDiffSnapshot) o);
    } else {
      return false;
    }
  }

}