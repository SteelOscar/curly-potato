import java.net.ConnectException

object HelperSSH {

    fun shutdownMaster() {

        val user = "root"
        val password = "12345678"

        try {

            kossh.impl.SSH.once("192.168.200.133", user, password, port = 1337) {
                println(execute("sudo shutdown -h now"))
            }
        } catch (e: ConnectException) {

            e.printStackTrace()
        }
    }
}