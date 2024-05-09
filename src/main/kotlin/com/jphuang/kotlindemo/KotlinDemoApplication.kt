package com.jphuang.kotlindemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*

@SpringBootApplication
class KotlinDemoApplication

fun main(args: Array<String>) {
    runApplication<KotlinDemoApplication>(*args)
}


@RestController
class MessageController (val service: MessageService){
    @GetMapping("/")
    fun index(@RequestParam("name", defaultValue = "man", required = false) name: String ="man") = "Hello World, $name!"


    @GetMapping("/list")
    fun list() = service.getMessage()
    @PostMapping("/")
    fun post(@RequestBody message: Message) {
        service.addMessage(message)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String) = service.getMessageById(id)
}

@Table("MESSAGES")
data class Message(@Id val id: String?, val text: String)

interface MessageRepository : CrudRepository<Message, String>
@Service
class  MessageService(private val  db: MessageRepository) {
    fun getMessage(): List<Message> = db.findAll().toList()
    fun addMessage(message: Message) {
        db.save(message)
    }
    fun getMessageById(id: String): Message? = db.findById(id).get()

    fun <T : Any> Optional<out T>.toList(): List<T> =
        if (isPresent) listOf(get()) else emptyList()
}