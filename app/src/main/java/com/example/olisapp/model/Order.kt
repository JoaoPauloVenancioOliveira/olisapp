package com.example.olisapp.model

import com.example.olisapp.enum.PaymentTypeEnum
import com.example.olisapp.enum.StatusEnum
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class Order(
    val id: Long,
    val client: String,
    val createdAt: String = formatOffsetDateTime(OffsetDateTime.now()),
    val finishedAt: String = formatOffsetDateTime(OffsetDateTime.now()),
    val items: List<Item>,
    val paymentType: PaymentTypeEnum,
    val status: StatusEnum
)

fun formatOffsetDateTime(offsetDateTime: OffsetDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val adjustedDateTime = offsetDateTime.withOffsetSameInstant(OffsetDateTime.now().offset)
    return formatter.format(adjustedDateTime)
}
