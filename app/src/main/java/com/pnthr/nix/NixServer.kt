package com.pnthr.nix

import android.app.Activity
import android.content.Context
import com.rk.exec.isTerminalInstalled
import com.rk.file.child
import com.rk.file.sandboxHomeDir
import com.rk.icons.Icon
import com.rk.lsp.LspConnectionConfig
import com.rk.lsp.ScriptedLspServer
import java.io.File

class NixServer(
    override val icon: Icon,
    override val supportedExtensions: List<String>,
    override val installScript: File,
) : ScriptedLspServer() {
    override val id = "nil"
    override val languageName = "Nix"
    override val serverName = "nil"
    override val installId = "nil language server"

    override suspend fun isInstalled(context: Context): Boolean {
        if (!isTerminalInstalled()) return false
        return sandboxHomeDir(context).child(".lsp/nil/nil").exists()
    }

    override fun install(activity: Activity) = launchInstaller(activity)

    override fun uninstall(activity: Activity) = launchInstaller(activity, "--uninstall")

    override fun update(activity: Activity) = launchInstaller(activity, "--update")

    override suspend fun isUpdatable(context: Context): Boolean = false

    override fun getConnectionConfig(): LspConnectionConfig {
        return LspConnectionConfig.Process(arrayOf("/home/.lsp/nil/nil"))
    }
}
