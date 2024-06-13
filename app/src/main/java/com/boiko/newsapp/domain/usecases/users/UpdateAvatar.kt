package com.boiko.newsapp.domain.usecases.users

import android.content.Context
import android.graphics.Bitmap
import com.boiko.newsapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

data class UpdateAvatar(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(context: Context, avatar: Bitmap): String {
        val file = File(context.cacheDir, "avatar")
        withContext(Dispatchers.IO) {
            file.createNewFile()
        }

        val bos = ByteArrayOutputStream()
        avatar.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bitmapData = bos.toByteArray()
        val fos = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        withContext(Dispatchers.IO) {
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        }

        return userRepository.updateAvatar(file)
    }
}