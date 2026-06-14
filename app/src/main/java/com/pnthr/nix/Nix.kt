package com.pnthr.nix

import android.app.Activity
import android.os.Bundle
import androidx.annotation.Keep
import com.rk.file.BuiltinFileType
import com.rk.file.child
import com.rk.file.sandboxHomeDir
import com.rk.lsp.LspRegistry
import com.rk.extension.ExtensionAPI
import com.rk.extension.ExtensionContext
import com.rk.utils.getTempDir

@Keep
@Suppress("unused")
class Nix(context: ExtensionContext) : ExtensionAPI(context) {
    private var nixServer: NixServer? = null

    override fun onExtensionLoaded() {
        val nilBinary = sandboxHomeDir(context.appContext).child(".lsp/nil/nil")
        nilBinary.parentFile?.mkdirs()
        if (!nilBinary.exists()) {
            context.assets.open("nil").use { input ->
                nilBinary.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            nilBinary.setExecutable(true)
        }

        val installScript = getTempDir().child("nix-lsp.sh").also {
            context.assets.open("nix-lsp.sh").use { input ->
                it.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        val nixFileType = BuiltinFileType.NIX

        nixServer = NixServer(
            icon = nixFileType.icon!!,
            supportedExtensions = nixFileType.extensions,
            installScript = installScript,
        ).also {
            LspRegistry.registerServer(it)
        }
    }

    private fun dispose() {
        nixServer?.let {
            LspRegistry.unregisterServer(it)
        }
    }

    override fun onInstalled() {}

    override fun onUpdated() {
        dispose()
        onExtensionLoaded()
    }

    override fun onUninstalled() {
        dispose()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
}
