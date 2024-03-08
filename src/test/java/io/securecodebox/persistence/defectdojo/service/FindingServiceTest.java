// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.config.Config;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


// This test is sufficient for all services (except user profile) as all the code is generic
// TODO: Add WireMock integration test.
class FindingServiceTest {
  private static final String FINDING_RESPONSE = """
     {
         "count": 1,
         "next": null,
         "previous": null,
         "results":
         [
             {
               "id": 42,
               "tags": [],
               "request_response": {
                 "req_resp": []
               },
               "accepted_risks": [],
               "push_to_jira": false,
               "age": 145,
               "sla_days_remaining": null,
               "finding_meta": [],
               "related_fields": null,
               "jira_creation": null,
               "jira_change": null,
               "display_status": "Active, Verified",
               "finding_groups": [],
               "title": "Open Port: 9929/TCP",
               "date": "2021-03-18",
               "sla_start_date": null,
               "cwe": 0,
               "cve": null,
               "cvssv3": null,
               "cvssv3_score": null,
               "url": null,
               "severity": "Info",
               "description": "### Host\\n\\n**IP Address:** 198.51.100.0\\n**FQDN:** scanme.nmap.org\\n\\n\\n**Port/Protocol:** 9929/tcp\\n\\n\\n\\n\\n",
               "mitigation": "N/A",
               "impact": "No impact provided",
               "steps_to_reproduce": null,
               "severity_justification": null,
               "references": null,
               "is_template": false,
               "active": true,
               "verified": true,
               "false_p": false,
               "duplicate": false,
               "out_of_scope": false,
               "risk_accepted": false,
               "under_review": false,
               "last_status_update": "2021-07-21T12:43:36.628994Z",
               "under_defect_review": false,
               "is_mitigated": false,
               "thread_id": 0,
               "mitigated": null,
               "numerical_severity": "S4",
               "last_reviewed": "2021-07-21T12:43:36.545348Z",
               "line_number": null,
               "sourcefilepath": null,
               "sourcefile": null,
               "param": null,
               "payload": null,
               "hash_code": "8dbaad23d4056f0a97bb8f487795fe392b4124f28d4049d16430e43415f1c219",
               "line": null,
               "file_path": null,
               "component_name": null,
               "component_version": null,
               "static_finding": false,
               "dynamic_finding": true,
               "created": "2021-07-21T12:43:36.549669Z",
               "scanner_confidence": null,
               "unique_id_from_tool": null,
               "vuln_id_from_tool": null,
               "sast_source_object": null,
               "sast_sink_object": null,
               "sast_source_line": null,
               "sast_source_file_path": null,
               "nb_occurences": null,
               "publish_date": null,
               "test": 222,
               "duplicate_finding": null,
               "review_requested_by": null,
               "defect_review_requested_by": null,
               "mitigated_by": null,
               "reporter": 5,
               "last_reviewed_by": 5,
               "sonarqube_issue": null,
               "endpoints": [
                 875
               ],
               "endpoint_status": [
                 8640
               ],
               "reviewers": [],
               "notes": [],
               "files": [],
               "found_by": [
                 132
               ]
             }
         ],
         "prefetch": {}
     }
    """;

  private final Config config = new Config("https://defectdojo.example.com", "abc", 42);
  private final FindingService sut = new FindingService(config);
  private final MockRestServiceServer server = MockRestServiceServer.createServer(sut.getRestTemplate());


  @Test
  void deserializeList_shouldResultExactlyOneResult() throws JsonProcessingException {
    var result = sut.deserializeList(FINDING_RESPONSE);

    assertEquals(1, result.getCount());
  }

  @Test
  void search() throws JsonProcessingException, URISyntaxException {
    final var url = String.format("%s/api/v2/%s/?offset=0&limit=100", config.getUrl(), sut.getUrlPath());
    server.expect(requestTo(url))
      .andRespond(withSuccess(FINDING_RESPONSE, MediaType.APPLICATION_JSON));

    assertIterableEquals(sut.deserializeList(FINDING_RESPONSE).getResults(), sut.search());
    server.verify();
  }
}
