package tests.suites

import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite
import tests.login.CredentialVerifications
import tests.project.ProjectEndpoint
import tests.step.StepEndpoint

@Suite
@SelectClasses(CredentialVerifications::class, StepEndpoint::class, ProjectEndpoint::class)
class SmokeTest