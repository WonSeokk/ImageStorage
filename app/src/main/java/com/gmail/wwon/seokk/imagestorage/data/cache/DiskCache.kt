package com.gmail.wwon.seokk.imagestorage.data.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.jakewharton.disklrucache.DiskLruCache
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiskCache @Inject constructor(@ApplicationContext private val context: Context) : Cache {

    private var cache: DiskLruCache = DiskLruCache.open(context.cacheDir, 1, 1, 10 * 1024 * 1024)

    override fun get(url: String): Bitmap? {
        val key = keyForDisk(url)
        val snapshot: DiskLruCache.Snapshot? = cache.get(key)
        return if (snapshot != null) {
            val inputStream: InputStream = snapshot.getInputStream(0)
            val buffIn = BufferedInputStream(inputStream, 8 * 1024)
            BitmapFactory.decodeStream(buffIn)
        } else {
            null
        }
    }

    override fun put(url: String, bitmap: Bitmap) {
        val key = keyForDisk(url)
        cache.edit(key)?.let {
            try {
                if (writeBitmapToFile(bitmap, it)) {
                    cache.flush()
                    it.commit()
                } else {
                    it.abort()
                }
            } catch (e: Exception) {
                try {
                    it?.abort()
                } catch (ignored: Exception) {
                }
            }
        }
    }

    override fun remove(url: String) {
        val key = keyForDisk(url)
        cache.remove(key)
    }

    override fun clear() {
        cache.delete()
        cache = DiskLruCache.open(context.cacheDir, 1, 1,
            10 * 1024 * 1024)
    }

    private fun writeBitmapToFile(bitmap: Bitmap, editor:
    DiskLruCache.Editor): Boolean {
        var out: OutputStream? = null
        try {
            out = BufferedOutputStream(editor.newOutputStream(0), 8 * 1024)
            return bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        } finally {
            out?.close()
        }
    }


    private fun keyForDisk(url: String): String? {
        val cacheKey: String? = try {
            val mDigest = MessageDigest.getInstance("MD5")
            mDigest.update(url.toByteArray())
            bytesToHexString(mDigest.digest())
        } catch (e: NoSuchAlgorithmException) {
            url.hashCode().toString()
        }
        return cacheKey
    }

    private fun bytesToHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }

}