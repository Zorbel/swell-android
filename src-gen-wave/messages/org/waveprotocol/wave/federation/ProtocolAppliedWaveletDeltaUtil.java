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

package org.waveprotocol.wave.federation;

import org.waveprotocol.wave.federation.ProtocolAppliedWaveletDelta.*;

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
import org.waveprotocol.wave.federation.ProtocolSignedDelta;
import org.waveprotocol.wave.federation.ProtocolHashedVersion;
import org.waveprotocol.wave.federation.ProtocolSignedDeltaUtil;
import org.waveprotocol.wave.federation.ProtocolHashedVersionUtil;
import org.waveprotocol.wave.communication.Blob;
import java.util.Iterator;
import java.util.List;

/**
 * Compares {@link ProtocolAppliedWaveletDelta}s for equality.
 *
 * Generated from org/waveprotocol/wave/federation/federation.protodevel. Do not edit.
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
public final class ProtocolAppliedWaveletDeltaUtil {
  private ProtocolAppliedWaveletDeltaUtil() {
  }

  /** Returns true if m1 and m2 are structurally equal. */
  public static boolean isEqual(ProtocolAppliedWaveletDelta m1, ProtocolAppliedWaveletDelta m2) {

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
    if (!ProtocolSignedDeltaUtil.isEqual(m1.getSignedOriginalDelta(), m2.getSignedOriginalDelta())) return false;

    /**
     * Licensed to the Apache Software Foundation (ASF) under one
     * or more contributor license agreements. See the NOTICE file
     * distributed with this work for additional information
     * regarding copyright ownership. The ASF licenses this file
     * to you under the Apache License, Version 2.0 (the
     * "License");
     you may not use this file except in compliance
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
    if (m1.hasHashedVersionAppliedAt() != m2.hasHashedVersionAppliedAt()) return false;
    if (m1.hasHashedVersionAppliedAt() && !ProtocolHashedVersionUtil.isEqual(m1.getHashedVersionAppliedAt(), m2.getHashedVersionAppliedAt())) return false;

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
    if (m1.getOperationsApplied() != m2.getOperationsApplied()) return false;

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
    if (m1.getApplicationTimestamp() != m2.getApplicationTimestamp()) return false;
    return true;
  }

  /** Returns true if m1 and m2 are equal according to isEqual. */
  public static boolean areAllEqual(List<? extends ProtocolAppliedWaveletDelta> m1,
  List<? extends ProtocolAppliedWaveletDelta> m2) {
    if (m1.size() != m2.size()) return false;
    Iterator<? extends ProtocolAppliedWaveletDelta> i1 = m1.iterator();
    Iterator<? extends ProtocolAppliedWaveletDelta> i2 = m2.iterator();
    while (i1.hasNext()) {
      if (!isEqual(i1.next(), i2.next())) return false;
    }
    return true;
  }

  /** Returns a structural hash code of message. */
  public static int getHashCode(ProtocolAppliedWaveletDelta message) {
    int result = 1;

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
    result = (31 * result) + message.getSignedOriginalDelta().hashCode();

    /**
     * Licensed to the Apache Software Foundation (ASF) under one
     * or more contributor license agreements. See the NOTICE file
     * distributed with this work for additional information
     * regarding copyright ownership. The ASF licenses this file
     * to you under the Apache License, Version 2.0 (the
     * "License");
     you may not use this file except in compliance
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
    result = (31 * result) + (message.hasHashedVersionAppliedAt() ? message.getHashedVersionAppliedAt().hashCode() : 0);

    /**
     * Licensed to the Apache Software Foundation (ASF) under one
     * or more contributor license agreements. See the NOTICE file
     * distributed with this work for additional information
     * regarding copyright ownership. The ASF licenses this file
     * to you under the Apache License, Version 2.0 (the
     * "License");
     you may not use this file except in compliance
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
    result = (31 * result) + Integer.valueOf(message.getOperationsApplied()).hashCode();

    /**
     * Licensed to the Apache Software Foundation (ASF) under one
     * or more contributor license agreements. See the NOTICE file
     * distributed with this work for additional information
     * regarding copyright ownership. The ASF licenses this file
     * to you under the Apache License, Version 2.0 (the
     * "License");
     you may not use this file except in compliance
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
    result = (31 * result) + Double.valueOf(message.getApplicationTimestamp()).hashCode();
    return result;
  }

}