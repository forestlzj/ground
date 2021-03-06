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

package dao.models.neo4j;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.Neo4jTest;
import exceptions.GroundVersionNotFoundException;
import models.models.NodeVersion;
import models.models.Tag;
import models.versions.GroundType;
import exceptions.GroundException;

import static org.junit.Assert.*;

public class Neo4jNodeVersionFactoryTest extends Neo4jTest {

  public Neo4jNodeVersionFactoryTest() throws GroundException {
    super();
  }

  @Test
  public void testNodeVersionCreation() throws GroundException {
    try {
      String nodeName = "testNode";
      long nodeId = Neo4jTest.createNode(nodeName).getId();

      String structureName = "testStructure";
      long structureId = Neo4jTest.createStructure(structureName).getId();
      long structureVersionId = Neo4jTest.createStructureVersion(structureId).getId();

      Map<String, Tag> tags = Neo4jTest.createTags();

      String testReference = "http://www.google.com";
      Map<String, String> parameters = new HashMap<>();
      parameters.put("http", "GET");

      long nodeVersionId = Neo4jTest.nodeVersionFactory.create(tags, structureVersionId,
          testReference, parameters, nodeId, new ArrayList<>()).getId();

      NodeVersion retrieved = Neo4jTest.nodeVersionFactory.retrieveFromDatabase(nodeVersionId);

      assertEquals(nodeId, retrieved.getNodeId());
      assertEquals(structureVersionId, retrieved.getStructureVersionId());
      assertEquals(testReference, retrieved.getReference());

      assertEquals(parameters.size(), retrieved.getParameters().size());
      assertEquals(tags.size(), retrieved.getTags().size());

      Map<String, String> retrievedParameters = retrieved.getParameters();
      Map<String, Tag> retrievedTags = retrieved.getTags();

      for (String key : parameters.keySet()) {
        assert (retrievedParameters).containsKey(key);
        assertEquals(parameters.get(key), retrievedParameters.get(key));
      }

      for (String key : tags.keySet()) {
        assert (retrievedTags).containsKey(key);
        assertEquals(tags.get(key), retrievedTags.get(key));
      }

      List<Long> leaves = Neo4jTest.nodeFactory.getLeaves(nodeName);

      assertTrue(leaves.contains(nodeVersionId));
      assertTrue(1 == leaves.size());
    } finally {
      Neo4jTest.neo4jClient.commit();
    }
  }

  @Test(expected = GroundException.class)
  public void testBadNodeVersion() throws GroundException {
    long id = 1;

    try {
      Neo4jTest.nodeVersionFactory.retrieveFromDatabase(id);
    } catch (GroundException e) {
      assertEquals(GroundVersionNotFoundException.class, e.getClass());
      throw e;
    } finally {
      Neo4jTest.neo4jClient.commit();
    }
  }
}
