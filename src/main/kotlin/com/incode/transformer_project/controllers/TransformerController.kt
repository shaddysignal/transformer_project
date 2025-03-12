package com.incode.transformer_project.controllers

import com.incode.transformer_project.Logging
import com.incode.transformer_project.model.TransformRequest
import com.incode.transformer_project.model.TransformResponse
import com.incode.transformer_project.service.TransformService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/")
class TransformerController(
    @Autowired private val transformService: TransformService,
) : Logging {

    @GetMapping(path = ["transform"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun transform(@RequestBody request: TransformRequest): ResponseEntity<TransformResponse> {
        val output = transformService.processTransformations(request.input, request.transformers)
        return ResponseEntity.ok(TransformResponse(output))
    }

}