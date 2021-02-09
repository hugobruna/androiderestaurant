package fr.isen.hugo.androiderestaurant

import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec


@RequiresApi(Build.VERSION_CODES.M)
fun encrypt(context: Context, strToEncrypt: String): ByteArray {
    /*
    var temp : String = strToEncrypt
    while (temp.toByteArray().size % 16 != 0){
        temp += "\u0020"
    }
    */
    val plainText = strToEncrypt.toByteArray(Charsets.UTF_8)
    val keygen = KeyGenerator.getInstance("AES")
    keygen.init(256)
    val key = keygen.generateKey()
    saveSecretKey(context, key)

    //val key: SecretKey = getSecretKey()
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")

    cipher.init(Cipher.ENCRYPT_MODE, key)
    saveInitializationVector(context, cipher.iv)
    val cipherText = cipher.doFinal(plainText)


    val sb = StringBuilder()
    for (b in cipherText) {
        sb.append(b.toChar())
    }
    return cipherText
}

@RequiresApi(Build.VERSION_CODES.M)
fun decrypt(context:Context, dataToDecrypt: ByteArray): String {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    val ivSpec = IvParameterSpec(getSavedInitializationVector(context))
    cipher.init(Cipher.DECRYPT_MODE, getSavedSecretKey(context), ivSpec)
    val cipherText = cipher.doFinal(dataToDecrypt)
    val sb = StringBuilder()
    for (b in cipherText) {
        sb.append(b.toChar())
    }
    return sb.toString()
}

fun saveSecretKey(context:Context, secretKey: SecretKey) {
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(secretKey)
    val strToSave = String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT))
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = sharedPref.edit()
    editor.putString("secret_key", strToSave)
    editor.apply()
}

fun getSavedSecretKey(context: Context): SecretKey {
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    val strSecretKey = sharedPref.getString("secret_key", "")
    val bytes = android.util.Base64.decode(strSecretKey, android.util.Base64.DEFAULT)
    val ois = ObjectInputStream(ByteArrayInputStream(bytes))
    val secretKey = ois.readObject() as SecretKey
    return secretKey
}

fun saveInitializationVector(context: Context, initializationVector: ByteArray) {
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(initializationVector)
    val strToSave = String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT))
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = sharedPref.edit()
    editor.putString("initialization_vector", strToSave)
    editor.apply()
}

fun getSavedInitializationVector(context: Context) : ByteArray {
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    val strInitializationVector = sharedPref.getString("initialization_vector", "")
    val bytes = android.util.Base64.decode(strInitializationVector, android.util.Base64.DEFAULT)
    val ois = ObjectInputStream(ByteArrayInputStream(bytes))
    val initializationVector = ois.readObject() as ByteArray
    return initializationVector
}


fun generateSecretKey(): SecretKey {
    val keyGenerator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
    } else {
        TODO("VERSION.SDK_INT < M")
    }
    val spec = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        KeyGenParameterSpec
                .Builder("Pedro_le_plot", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
    } else {
        TODO("VERSION.SDK_INT < M")
    }

    keyGenerator.init(spec)
    return keyGenerator.generateKey()
}


fun getSecretKey(): SecretKey {
    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)
    val secretKeyEntry: KeyStore.Entry? = keyStore.getEntry("Pedro_le_plot", null)
    return if(secretKeyEntry !is KeyStore.SecretKeyEntry){
        generateSecretKey()
    } else {
        secretKeyEntry.secretKey
    }

}