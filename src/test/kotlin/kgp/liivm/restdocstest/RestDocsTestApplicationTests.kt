package kgp.liivm.restdocstest

import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.Filter
import io.restassured.specification.RequestSpecification
import kgp.liivm.restdocstest.acceptance.AcceptanceTest
import kgp.liivm.restdocstest.acceptance.getResource
import kgp.liivm.restdocstest.acceptance.givenRequestSpecification
import kgp.liivm.restdocstest.acceptance.postResource
import kgp.liivm.restdocstest.dsl.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration

@ExtendWith(value = [RestDocumentationExtension::class])
class RestDocsTestApplicationTests : AcceptanceTest() {
    lateinit var restDocumentSpec: RequestSpecification

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        this.restDocumentSpec = RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation))
            .build()
    }

    @Test
    fun test01() {
        val response = getResource(
            request = givenRequestSpecification(restDocumentSpec, documentFilter01()),
            url = "/api/{teamId}",
            queryParams = mapOf("teamName" to "My Team"),
            pathParams = mapOf("teamId" to "1")
        )
    }

    @Test
    fun test02() {
        val response = postResource(
            request = givenRequestSpecification(restDocumentSpec, documentFilter02()),
            url = "/api",
            body = mapOf(
                "teamId" to "1",
                "teamName" to "My Team"
            )
        )
    }

    fun documentFilter01(): Filter {
        return RestAssuredRestDocumentationWrapper.document(
            identifier = "get api",
            requestPreprocessor = preprocessRequest(prettyPrint()),
            responsePreprocessor = preprocessResponse(prettyPrint()),
            queryParameters(
                "teamName" means "team name" isOptional true
            ),
            pathParameters(
                "teamId" means "the team id" isOptional false
            ),
            responseFields(
                "status" type NUMBER means "status",
                "message" type STRING means "message",
                "data" type OBJECT means "data",
                "data.name" type STRING means "team name",
                "data.status" type STRING means "team status - ${Status.entries.map { it.name }}",
                "data.registeredDateTime" type DATETIME means "team registered date time",
                "data.members" type ARRAY means "team members",
                "data.members[].name" type STRING means "team member name",
                "data.members[].registeredDateTime" type DATETIME means "team member registered date time",
                "data.members[].age" type NUMBER means "team member age",
                "data.members[].isMarried" type BOOLEAN means "team member isMarried",
            )
        )
    }

    fun documentFilter02(): Filter {
        return RestAssuredRestDocumentationWrapper.document(
            identifier = "post api",
            requestPreprocessor = preprocessRequest(prettyPrint()),
            responsePreprocessor = preprocessResponse(prettyPrint()),
            requestFields(
                "teamId" type STRING means "team id",
                "teamName" type STRING means "team name"
            ),
            responseFields(
                "status" type NUMBER means "status",
                "message" type STRING means "message",
                "data" type OBJECT means "data",
                "data.name" type STRING means "team name",
                "data.status" type STRING means "team status - ${Status.entries.map { it.name }}",
                "data.registeredDateTime" type DATETIME means "team registered date time",
                "data.members" type ARRAY means "team members",
                "data.members[].name" type STRING means "team member name",
                "data.members[].registeredDateTime" type DATETIME means "team member registered date time",
                "data.members[].age" type NUMBER means "team member age",
                "data.members[].isMarried" type BOOLEAN means "team member isMarried",
            )
        )
    }
}
