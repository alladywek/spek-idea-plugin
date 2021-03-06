package org.jetbrains.spek.idea

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.asJava.classes.KtLightClass

/**
 * @author Ranie Jade Ramiso
 */
class SpekJvmImplicitUsageProvider: ImplicitUsageProvider {
    override fun isImplicitWrite(element: PsiElement) = false

    override fun isImplicitRead(element: PsiElement) = false

    override fun isImplicitUsage(element: PsiElement): Boolean {
        if (element is KtLightClass) {
            return SpekJvmUtils.isSpec(element)
        }
        return false
    }
}
