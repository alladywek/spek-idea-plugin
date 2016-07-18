package org.jetbrains.spek.idea.tooling.execution

import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.discovery.DiscoverySelectors
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory

/**
 * @author Ranie Jade Ramiso
 */
class SpekTestRunner(val spec: String) {
    fun run() {
        val request = LauncherDiscoveryRequestBuilder.request()
            .selectors(
                DiscoverySelectors.selectClass(spec)
            )
            .build()

        val launcher = LauncherFactory.create()
        launcher.registerTestExecutionListeners(object: TestExecutionListener {
            override fun executionFinished(testIdentifier: TestIdentifier, testExecutionResult: TestExecutionResult) {
                val name = testIdentifier.displayName
                if (testIdentifier.isContainer) {
                    out("testSuiteFinished name='$name'")
                } else {
                    if (testExecutionResult.status != TestExecutionResult.Status.SUCCESSFUL) {
                        val throwable = testExecutionResult.throwable.get()
                        out("testFailed name='$name' message='${throwable.message}'")
                        throwable.printStackTrace(System.err)

                    } else {
                        out("testFinished name='$name'")
                    }
                }
            }

            override fun executionStarted(testIdentifier: TestIdentifier) {
                val name = testIdentifier.displayName
                if (testIdentifier.isContainer) {
                    out("testSuiteStarted name='$name'")
                } else {
                    out("testStarted name='$name'")
                }
            }

            override fun testPlanExecutionStarted(testPlan: TestPlan) {
                out("enteredTheMatrix")
            }

            override fun executionSkipped(testIdentifier: TestIdentifier, reason: String) {
                val name = testIdentifier.displayName
                out("testIgnored name='$name' ignoreComment='$reason'")
                out("testFinished name='$name'")
            }
        })

        launcher.execute(request)
    }

    private fun out(event: String) {
        println("##teamcity[$event]")
    }
}
