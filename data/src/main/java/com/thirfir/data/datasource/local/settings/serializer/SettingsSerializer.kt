package com.thirfir.data.datasource.local.settings.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.thirfir.data.SettingsProto
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<SettingsProto> {
    override val defaultValue: SettingsProto = SettingsProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingsProto {
        try {
            return SettingsProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: SettingsProto,
        output: OutputStream) = t.writeTo(output)
}