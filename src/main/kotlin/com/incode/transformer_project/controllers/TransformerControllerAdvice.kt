package com.incode.transformer_project.controllers

import com.incode.transformer_project.Logging
import com.incode.transformer_project.log
import com.incode.transformer_project.model.TransformResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice(basePackageClasses = [TransformerController::class])
class TransformerControllerAdvice : Logging, ResponseEntityExceptionHandler() {

    @ResponseBody
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(request: HttpServletRequest, ex: Throwable): ResponseEntity<TransformResponse> {
        log.warn { "Exception during transform request, exception=${ex.message}" }

        return ResponseEntity.badRequest().body(TransformResponse(error = ex.message))
    }

}