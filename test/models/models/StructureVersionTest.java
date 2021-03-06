/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models.models;

import models.versions.GroundType;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static util.ModelTestUtils.*;

public class StructureVersionTest {

  @Test
  public void serializesToJSON() throws Exception {
    Map<String, GroundType> attributes = new HashMap<>();
    attributes.put("tag1", GroundType.INTEGER);
    attributes.put("tag2", GroundType.STRING);

    StructureVersion structureVersion = new StructureVersion(1, 1, attributes);

    final String expected = convertFromClassToString(convertFromStringToClass(readFromFile
        ("test/resources/fixtures/models/structure_version.json"), StructureVersion.class));
    assertThat(convertFromClassToString(structureVersion)).isEqualTo(expected);
  }

  @Test
  public void deserializesFromJSON() throws Exception {
    Map<String, GroundType> attributes = new HashMap<>();
    attributes.put("tag1", GroundType.INTEGER);
    attributes.put("tag2", GroundType.STRING);

    StructureVersion structureVersion = new StructureVersion(1, 1, attributes);

    assertEquals(convertFromStringToClass(
        readFromFile("test/resources/fixtures/models/structure_version.json"),
        StructureVersion.class), structureVersion);
  }

  @Test
  public void testStructureVersionNotEquals() throws Exception {
    StructureVersion truth = new StructureVersion(1, 2, new HashMap<>());
    assertFalse(truth.equals("notStructureVersion"));

    StructureVersion differentId = new StructureVersion(10, 2, new HashMap<>());
    assertFalse(truth.equals(differentId));

    StructureVersion differentStructureId = new StructureVersion(1, 10, new HashMap<>());
    assertFalse(truth.equals(differentStructureId));

    Map<String, GroundType> attributes = new HashMap<>();
    attributes.put("test", GroundType.BOOLEAN);
    StructureVersion differentAttributes = new StructureVersion(1, 2, attributes);
    assertFalse(truth.equals(differentAttributes));
  }
}
