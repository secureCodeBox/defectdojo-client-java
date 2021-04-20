/*
 *  secureCodeBox (SCB)
 *  Copyright 2021 iteratec GmbH
 *  https://www.iteratec.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.securecodebox.persistence.defectdojo;

import lombok.Getter;

public enum ScanType {
  ACUNETIX_SCAN("Acunetix Scan"),
  ANCHORE_ENGINE_SCAN("Anchore Engine Scan"),
  ANCHORE_ENTERPRISE_POLICY_CHECK("Anchore Enterprise Policy Check"),
  ANCHORE_GRYPE_SCAN("Anchore Grype Scan"),
  API_TEST("API Test"),
  APP_SPIDER_SCAN("AppSpider Scan"),
  AQUA_SCAN("Aqua Scan"),
  ARACHNI_SCAN("Arachni Scan"),
  AWS_PROWLER_SCANNER("AWS Prowler Scanner"),
  AWS_SCOUT2_SCANNER("AWS Scout2 Scanner"),
  AWS_SECURITY_HUB_SCAN("AWS Security Hub Scan"),
  BANDIT_SCAN("Bandit Scan"),
  BLACKDUCK_COMPONENT_RISK("Blackduck Component Risk"),
  BLACKDUCK_HUB_SCAN("Blackduck Hub Scan"),
  BRAKEMAN_SCAN("Brakeman Scan"),
  BUG_CROWD("BugCrowd "),
  BUNDLER_AUDIT_SCAN("Bundler-Audit Scan"),
  BURP_SCAN("Burp Scan"),
  BURP_API_SCAN("Burp REST API"),
  BURP_SUITE_ENTERPRISE("Burp Suite Enterprise"),
  CCVS_REPORT("CCVS Report"),
  CHECKMARX_SCAN("Checkmarx Scan"),
  CHOCTAW_HOG_SCAN("Choctaw Hog Scan"),
  CLAIR_KLAR_SCAN("Clair Klar Scan"),
  CLAIR_SCAN("Clair Scan"),
  COBALT_IO_SCAN("Cobalt.io Scan"),
  CONTRAST_SCAN("Contrast Scan"),
  CRASHTEST_SECURITY_JSON_SCAN("Crashtest Security JSON File"),
  CRASHTEST_SECURITY_XML_SCAN("Crashtest Security XML File"),
  DAWN_SCANNER_SCAN("DawnScanner Scan"),
  DEPENDENCY_CHECK_SCAN("Dependency Check Scan"),
  DEPENDENCY_TRACK_FINDING_PACKAGING_FORMAT_FPF_EXPORT("Dependency Track Finding Packaging Format (FPF) Export"),
  DR_HEADER_JSON_IMPORTER("DrHeader JSON Importer"),
  DSOP_SCAN("DSOP Scan"),
  ES_LINT_SCAN("ESLint Scan"),
  FORTIFY("Fortify"),
  GENERIC_SCB_FINDINGS_IMPORT("SCB Scan"),
  GITHUB_VULNERABILITY_SCAN("Github Vulnerability Scan"),
  GITLAB_SAST_REPORT("GitLab SAST Report"),
  GITLAB_DEPENDENCY_SCANNING_REPORT("GitLab Dependency Scanning Report"),
  GITLEAKS_SCAN("Gitleaks Scan"),
  GOSEC_SCANNER("Gosec Scanner"),
  HACKER_ONE_CASES("HackerOne Cases"),
  HADOLINT_DOCKERFILE_CHECK("Hadolint Dockerfile check"),
  HARBOR_VULNERABILITY_SCAN("Harbor Vulnerability Scan"),
  HUSKY_CI_REPORT("HuskyCI Report"),
  IBM_APP_SCAN_DAST("IBM AppScan DAST"),
  IMMUNIWEB_SCAN("Immuniweb Scan"),
  J_FROG_XRAY_SCAN("JFrog Xray Scan"),
  KIUWAN_SCAN("Kiuwan Scan"),
  KUBEBENCH_SCAN("kube-bench Scan"),
  MANUAL_CODE_REVIEW("Manual Code Review"),
  MICROFOCUS_WEBINSPECT_SCAN("Microfocus Webinspect Scan"),
  MOB_SF_SCANNER("MobSF Scanner"),
  MOZILLA_OBSERVATORY_SCAN("Mozilla Observatory Scan"),
  NESSUS_SCAN("Nessus Scan"),
  NETSPARKER_SCAN("Netsparker Scan"),
  NEXPOSE_SCAN("Nexpose Scan"),
  NIKTO_XML_SCAN("Nikto Scan"),
  NMAP_XML_SCAN("Nmap Scan"),
  NODE_SECURITY_PLATFORM_SCAN("Node Security Platform Scan"),
  NPM_AUDIT_SCAN("NPM Audit Scan"),
  OPENSCAP_VULNERABILITY_SCAN("Openscap Vulnerability Scan"),
  OPEN_VAS_CSV("OpenVAS CSV"),
  ORT_MODEL_IMPORTER("ORT evaluated model Importer"),
  OSSINDEX_DEVAUDIT_SCAN_IMPORTER("OssIndex Devaudit SCA Scan Importer"),
  OUTPOST24_SCAN("Outpost24 Scan"),
  PEN_TEST("Pen Test"),
  PHP_SECURITY_AUDIT_V2("PHP Security Audit v2"),
  PHP_SYMFONY_SECURITY_CHECKER("PHP Symfony Security Checker"),
  QUALYS_INFRASTRUCTURE_SCAN_WEB_GUI_XML("Qualys Infrastructure Scan (WebGUI XML)"),
  QUALYS_SCAN("Qualys Scan"),
  QUALYS_WEBAPP_SCAN("Qualys Webapp Scan"),
  RETIRE_JS_SCAN("Retire.js Scan"),
  RISK_RECON_API_IMPORTER("Risk Recon API Importer"),
  SAFETY_SCAN("Safety Scan"),
  SARIF_SCAN("SARIF"),
  SECURITY_RESEARCH("Security Research"),
  SKF_SCAN("SKF Scan"),
  SNYK_SCAN("Snyk Scan"),
  SONAR_QUBE_SCAN("SonarQube Scan"),
  SONAR_QUBE_API_IMPORT("SonarQube API Import"),
  SONAR_QUBE_DETAILED_SCAN("SonarQube Scan detailed"),
  SONATYPE_APPLICATION_SCAN("Sonatype Application Scan"),
  SPOTBUGS_XML_SCAN("SpotBugs Scan"),
  SSLSCAN("Sslscan"),
  SSL_LABS_SCAN("SSL Labs Scan"),
  SSLYZE_3_JSON_SCAN("SSLyze 3 Scan (JSON)"),
  SSLYZE_XML_SCAN("Sslyze Scan"),
  STATIC_CHECK("Static Check"),
  TESTSSL_SCAN("Testssl Scan"),
  THREAT_MODELING("Threat Modeling"),
  TRIVY_SCAN("Trivy Scan"),
  TRUFFLEHOG("Trufflehog"),
  TRUSTWAVE_CSV_SCAN("Trustwave Scan (CSV)"),
  TWISTLOCK_IMAGE_SCAN("Twistlock Image Scan"),
  VCG_SCAN("VCG Scan"),
  VERACODE_SCAN("Veracode Scan"),
  WAPITI_SCAN("Wapiti Scan"),
  WEB_APPLICATION_TEST("Web Application Test"),
  WHITESOURCE_SCAN("Whitesource Scan"),
  WPSCAN_JSON("Wpscan"),
  XANITIZER_SCAN("Xanitizer Scan"),
  YARN_AUDIT_SCAN("Yarn Audit Scan"),
  ZAP_SCAN("ZAP Scan"),
  ;

  @Getter
  String testType;

  ScanType(String testType) {
    this.testType = testType;
  }
}
