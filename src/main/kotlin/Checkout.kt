package com.checkout

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@EnableAutoConfiguration
@ComponentScan
class Checkout

fun main(args: Array<String>) {
    runApplication<Checkout>(*args)
}