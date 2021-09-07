package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.models.Finding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FindingServiceTest {

    DefectDojoConfig config;
    FindingService underTest;

    String findingResponse = "{\n" +
            "  \"count\": 1,\n" +
            "  \"next\": null,\n" +
            "  \"previous\": null,\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"id\": 42,\n" +
            "      \"tags\": [],\n" +
            "      \"request_response\": {\n" +
            "        \"req_resp\": []\n" +
            "      },\n" +
            "      \"accepted_risks\": [],\n" +
            "      \"push_to_jira\": false,\n" +
            "      \"age\": 145,\n" +
            "      \"sla_days_remaining\": null,\n" +
            "      \"finding_meta\": [],\n" +
            "      \"related_fields\": null,\n" +
            "      \"jira_creation\": null,\n" +
            "      \"jira_change\": null,\n" +
            "      \"display_status\": \"Active, Verified\",\n" +
            "      \"finding_groups\": [],\n" +
            "      \"title\": \"Open Port: 9929/TCP\",\n" +
            "      \"date\": \"2021-03-18\",\n" +
            "      \"sla_start_date\": null,\n" +
            "      \"cwe\": 0,\n" +
            "      \"cve\": null,\n" +
            "      \"cvssv3\": null,\n" +
            "      \"cvssv3_score\": null,\n" +
            "      \"url\": null,\n" +
            "      \"severity\": \"Info\",\n" +
            "      \"description\": \"### Host\\n\\n**IP Address:** 198.51.100.0\\n**FQDN:** scanme.nmap.org\\n\\n\\n**Port/Protocol:** 9929/tcp\\n\\n\\n\\n\\n\",\n" +
            "      \"mitigation\": \"N/A\",\n" +
            "      \"impact\": \"No impact provided\",\n" +
            "      \"steps_to_reproduce\": null,\n" +
            "      \"severity_justification\": null,\n" +
            "      \"references\": null,\n" +
            "      \"is_template\": false,\n" +
            "      \"active\": true,\n" +
            "      \"verified\": true,\n" +
            "      \"false_p\": false,\n" +
            "      \"duplicate\": false,\n" +
            "      \"out_of_scope\": false,\n" +
            "      \"risk_accepted\": false,\n" +
            "      \"under_review\": false,\n" +
            "      \"last_status_update\": \"2021-07-21T12:43:36.628994Z\",\n" +
            "      \"under_defect_review\": false,\n" +
            "      \"is_mitigated\": false,\n" +
            "      \"thread_id\": 0,\n" +
            "      \"mitigated\": null,\n" +
            "      \"numerical_severity\": \"S4\",\n" +
            "      \"last_reviewed\": \"2021-07-21T12:43:36.545348Z\",\n" +
            "      \"line_number\": null,\n" +
            "      \"sourcefilepath\": null,\n" +
            "      \"sourcefile\": null,\n" +
            "      \"param\": null,\n" +
            "      \"payload\": null,\n" +
            "      \"hash_code\": \"8dbaad23d4056f0a97bb8f487795fe392b4124f28d4049d16430e43415f1c219\",\n" +
            "      \"line\": null,\n" +
            "      \"file_path\": null,\n" +
            "      \"component_name\": null,\n" +
            "      \"component_version\": null,\n" +
            "      \"static_finding\": false,\n" +
            "      \"dynamic_finding\": true,\n" +
            "      \"created\": \"2021-07-21T12:43:36.549669Z\",\n" +
            "      \"scanner_confidence\": null,\n" +
            "      \"unique_id_from_tool\": null,\n" +
            "      \"vuln_id_from_tool\": null,\n" +
            "      \"sast_source_object\": null,\n" +
            "      \"sast_sink_object\": null,\n" +
            "      \"sast_source_line\": null,\n" +
            "      \"sast_source_file_path\": null,\n" +
            "      \"nb_occurences\": null,\n" +
            "      \"publish_date\": null,\n" +
            "      \"test\": 222,\n" +
            "      \"duplicate_finding\": null,\n" +
            "      \"review_requested_by\": null,\n" +
            "      \"defect_review_requested_by\": null,\n" +
            "      \"mitigated_by\": null,\n" +
            "      \"reporter\": 5,\n" +
            "      \"last_reviewed_by\": 5,\n" +
            "      \"sonarqube_issue\": null,\n" +
            "      \"endpoints\": [\n" +
            "        875\n" +
            "      ],\n" +
            "      \"endpoint_status\": [\n" +
            "        8640\n" +
            "      ],\n" +
            "      \"reviewers\": [],\n" +
            "      \"notes\": [],\n" +
            "      \"files\": [],\n" +
            "      \"found_by\": [\n" +
            "        132\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"prefetch\": {}\n" +
            "}";

    @BeforeEach
    void setup() {
        config = new DefectDojoConfig("https://defectdojo.example.com", "abc", "test-user", 42);
        underTest = new FindingService(config);
    }

    @Test
    void deserializeList() throws JsonProcessingException {
        var foo = underTest.deserializeList(findingResponse);

        assertEquals(1, foo.getCount());
    }
}