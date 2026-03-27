package tests.suites

import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite
import tests.generation.textToImage.MakoTextToImageTests
import tests.generation.textToImage.NotuAiTextToImageTests
import tests.login.CredentialVerifications
import tests.project.ProjectEndpoint
import tests.step.StepEndpoint
import tests.generation.textToImage.GenV2TextToImageTests

@Suite
@SelectClasses(CredentialVerifications::class, StepEndpoint::class, ProjectEndpoint::class
, NotuAiTextToImageTests::class, MakoTextToImageTests::class, GenV2TextToImageTests::class)
class SmokeTest