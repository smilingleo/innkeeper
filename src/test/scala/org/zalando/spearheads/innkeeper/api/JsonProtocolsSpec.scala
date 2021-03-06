package org.zalando.spearheads.innkeeper.api

import java.time.LocalDateTime

import org.scalatest.{FunSpec, Matchers}
import spray.json._
import JsonProtocols._
import scala.collection.immutable.Seq

class JsonProtocolsSpec extends FunSpec with Matchers {

  describe("Predicate") {
    it("should unmarshall the predicate") {
      val predicate = """{
                        |  "name": "somePredicate",
                        |  "args": [{
                        |    "value": "Hello",
                        |    "type": "string"
                        |  }, {
                        |    "value": "123",
                        |    "type": "number"
                        |  }, {
                        |    "value": "0.99",
                        |    "type": "number"
                        |  }, {
                        |    "value": "1",
                        |    "type": "number"
                        |  }]
                        |}""".stripMargin.parseJson.convertTo[Predicate]

      predicate.name should be("somePredicate")
      predicate.args(0) should be(StringArg("Hello"))
      predicate.args(1) should be(NumericArg("123"))
      predicate.args(2) should be(NumericArg("0.99"))
      predicate.args(3) should be(NumericArg("1"))
    }

    it("should marshall the Predicate") {
      val predicateJson = Predicate(
        "somePredicate",
        Seq(
          StringArg("Hello"),
          NumericArg("123"),
          NumericArg("0.99"),
          NumericArg("1"))).toJson

      predicateJson.prettyPrint should be(
        """{
          |  "name": "somePredicate",
          |  "args": [{
          |    "value": "Hello",
          |    "type": "string"
          |  }, {
          |    "value": "123",
          |    "type": "number"
          |  }, {
          |    "value": "0.99",
          |    "type": "number"
          |  }, {
          |    "value": "1",
          |    "type": "number"
          |  }]
          |}""".stripMargin)
    }
  }

  describe("Filter") {
    it("should unmarshall the Filter") {
      val filter = """{
                     |  "name": "someFilter",
                     |  "args": [{
                     |    "value": "Hello",
                     |    "type": "string"
                     |  }, {
                     |    "value": "123",
                     |    "type": "number"
                     |  }, {
                     |    "value": "0.99",
                     |    "type": "number"
                     |  }, {
                     |    "value": "1",
                     |    "type": "number"
                     |  }]
                     |}""".stripMargin.parseJson.convertTo[Filter]

      filter.name should be("someFilter")
      filter.args(0) should be(StringArg("Hello"))
      filter.args(1) should be(NumericArg("123"))
      filter.args(2) should be(NumericArg("0.99"))
      filter.args(3) should be(NumericArg("1"))
    }

    it("should marshall the Filter") {
      val filterJson = Filter(
        "someFilter",
        Seq(
          StringArg("Hello"),
          NumericArg("123"),
          NumericArg("0.99"),
          NumericArg("1"))).toJson

      filterJson.prettyPrint should be(
        """{
          |  "name": "someFilter",
          |  "args": [{
          |    "value": "Hello",
          |    "type": "string"
          |  }, {
          |    "value": "123",
          |    "type": "number"
          |  }, {
          |    "value": "0.99",
          |    "type": "number"
          |  }, {
          |    "value": "1",
          |    "type": "number"
          |  }]
          |}""".stripMargin)
    }
  }

  describe("New") {
    it("should unmarshall a simple NewRoute") {
      val route = """{ }""".stripMargin.parseJson.convertTo[NewRoute]
      route.filters.get should (be(Seq.empty))
      route.predicates.get should (be(Seq.empty))
    }

    it("should marshall the NewRoute") {
      val routeJson = NewRoute(
        predicates = Some(Seq(
          Predicate("somePredicate", Seq(StringArg("Hello"), NumericArg("123"))),
          Predicate("someOtherPredicate", Seq(StringArg("Hello"), NumericArg("123"), StringArg("World")))
        )),
        filters = Some(Seq(
          Filter("someFilter", Seq(StringArg("Hello"), NumericArg("123"))),
          Filter("someOtherFilter", Seq(StringArg("Hello"), NumericArg("123"),
            StringArg("World")))
        )),
        endpoint = Some("https://www.endpoint.com:8080/endpoint")
      ).toJson

      routeJson.prettyPrint should be {
        """{
          |  "predicates": [{
          |    "name": "somePredicate",
          |    "args": [{
          |      "value": "Hello",
          |      "type": "string"
          |    }, {
          |      "value": "123",
          |      "type": "number"
          |    }]
          |  }, {
          |    "name": "someOtherPredicate",
          |    "args": [{
          |      "value": "Hello",
          |      "type": "string"
          |    }, {
          |      "value": "123",
          |      "type": "number"
          |    }, {
          |      "value": "World",
          |      "type": "string"
          |    }]
          |  }],
          |  "filters": [{
          |    "name": "someFilter",
          |    "args": [{
          |      "value": "Hello",
          |      "type": "string"
          |    }, {
          |      "value": "123",
          |      "type": "number"
          |    }]
          |  }, {
          |    "name": "someOtherFilter",
          |    "args": [{
          |      "value": "Hello",
          |      "type": "string"
          |    }, {
          |      "value": "123",
          |      "type": "number"
          |    }, {
          |      "value": "World",
          |      "type": "string"
          |    }]
          |  }],
          |  "endpoint": "https://www.endpoint.com:8080/endpoint"
          |}""".stripMargin
      }
    }

    it("should marshall a minimal NewRoute") {
      val routeJson = NewRoute(predicates = Some(Seq(
        Predicate("somePredicate", Seq(StringArg("Hello"), NumericArg("123")))))).toJson

      routeJson.prettyPrint should be {
        """{
          |  "predicates": [{
          |    "name": "somePredicate",
          |    "args": [{
          |      "value": "Hello",
          |      "type": "string"
          |    }, {
          |      "value": "123",
          |      "type": "number"
          |    }]
          |  }],
          |  "filters": []
          |}""".stripMargin
      }
    }
  }

  describe("RouteIn") {

    val newRoute = NewRoute(
      predicates = Some(Seq(
        Predicate("somePredicate", Seq(StringArg("Hello"), NumericArg("123")))))
    )

    val routeIn = RouteIn(
      1L,
      RouteName("THE_ROUTE"),
      newRoute,
      usesCommonFilters = false,
      Some(LocalDateTime.of(2015, 10, 10, 10, 10, 10)),
      Some(LocalDateTime.of(2015, 11, 11, 11, 11, 11)),
      Some("this is a route"),
      Some(Seq(1L))
    )

    it("should unmarshall the RouteIn") {
      val route = """{
                    |  "name": "THE_ROUTE",
                    |  "description": "this is a route",
                    |  "activate_at": "2015-10-10T10:10:10",
                    |  "disable_at": "2015-11-11T11:11:11",
                    |  "predicates": [{
                    |    "name": "somePredicate",
                    |    "args": [{
                    |    "value": "Hello",
                    |    "type": "string"
                    |  }, {
                    |    "value": "123",
                    |    "type": "number"
                    |  }]
                    |  }],
                    |  "filters": [],
                    |  "path_id": 1,
                    |  "uses_common_filters": false,
                    |  "host_ids": [1]
                    |}""".stripMargin.parseJson.convertTo[RouteIn]
      route should be(routeIn)
    }

    it("should marshall the RouteIn") {
      routeIn.toJson.prettyPrint should be {
        """{
          |  "name": "THE_ROUTE",
          |  "predicates": [{
          |    "name": "somePredicate",
          |    "args": [{
          |      "value": "Hello",
          |      "type": "string"
          |    }, {
          |      "value": "123",
          |      "type": "number"
          |    }]
          |  }],
          |  "host_ids": [1],
          |  "description": "this is a route",
          |  "uses_common_filters": false,
          |  "activate_at": "2015-10-10T10:10:10",
          |  "disable_at": "2015-11-11T11:11:11",
          |  "filters": [],
          |  "path_id": 1
          |}""".stripMargin
      }
    }
  }

  describe("RouteOut") {

    val newRoute = NewRoute(
      predicates = Some(Seq(
        Predicate("somePredicate", Seq(StringArg("Hello"), NumericArg("123")))))
    )

    val routeOut = RouteOut(
      1,
      1L,
      RouteName("THE_ROUTE"),
      newRoute,
      LocalDateTime.of(2015, 10, 10, 10, 10, 10),
      LocalDateTime.of(2015, 10, 10, 10, 10, 10),
      UserName("user"),
      usesCommonFilters = false,
      Some(LocalDateTime.of(2015, 11, 11, 11, 11, 11)),
      Some("this is a route"),
      Some(Seq(1L)),
      None,
      None
    )

    val routeOutWithEmbedded =
      routeOut.copy(
        path = Some(pathOut),
        hosts = Some(Seq(host)))

    it("should marshall the RouteOut") {
      routeOut.toJson.prettyPrint should be {
        """{
          |  "created_by": "user",
          |  "name": "THE_ROUTE",
          |  "predicates": [{
          |    "name": "somePredicate",
          |    "args": [{
          |      "value": "Hello",
          |      "type": "string"
          |    }, {
          |      "value": "123",
          |      "type": "number"
          |    }]
          |  }],
          |  "host_ids": [1],
          |  "description": "this is a route",
          |  "uses_common_filters": false,
          |  "activate_at": "2015-10-10T10:10:10",
          |  "id": 1,
          |  "disable_at": "2015-11-11T11:11:11",
          |  "filters": [],
          |  "created_at": "2015-10-10T10:10:10",
          |  "path_id": 1
          |}""".stripMargin
      }
    }

    it("should marshall the RouteOut with embedded objects") {
      routeOutWithEmbedded.toJson.prettyPrint should be {
        """{
          |  "created_by": "user",
          |  "name": "THE_ROUTE",
          |  "predicates": [{
          |    "name": "somePredicate",
          |    "args": [{
          |      "value": "Hello",
          |      "type": "string"
          |    }, {
          |      "value": "123",
          |      "type": "number"
          |    }]
          |  }],
          |  "path": {
          |    "created_by": "username",
          |    "owned_by_team": "team",
          |    "host_ids": [1, 2, 3],
          |    "uri": "/hello",
          |    "has_star": false,
          |    "id": 1,
          |    "is_regex": false,
          |    "created_at": "2015-10-10T10:10:10",
          |    "updated_at": "2016-10-10T10:10:10"
          |  },
          |  "host_ids": [1],
          |  "description": "this is a route",
          |  "uses_common_filters": false,
          |  "activate_at": "2015-10-10T10:10:10",
          |  "id": 1,
          |  "disable_at": "2015-11-11T11:11:11",
          |  "hosts": [{
          |    "id": 1,
          |    "name": "name"
          |  }],
          |  "filters": [],
          |  "created_at": "2015-10-10T10:10:10",
          |  "path_id": 1
          |}""".stripMargin
      }
    }
  }

  describe("RoutePatch") {
    it("should unmarshall the RoutePatch") {
      val predicate = Predicate("somePredicate", Seq(StringArg("Hello"), NumericArg("123")))
      val filter = Filter("someFilter", Seq(StringArg("World"), NumericArg("321")))
      val expectedRouteData = NewRoute(
        predicates = Some(Seq(predicate)),
        filters = Some(Seq(filter)),
        endpoint = Some("some-endpoint.com")
      )
      val expected = RoutePatch(Some(expectedRouteData), Some(false), Some("route description"), Some(Seq(1L, 2L)))

      val routePatchString =
        """{
          |  "description": "route description",
          |  "uses_common_filters": false,
          |  "host_ids": [1, 2],
          |  "route": {
          |    "endpoint": "some-endpoint.com",
          |    "predicates": [{
          |       "name": "somePredicate",
          |       "args": [{
          |        "value": "Hello",
          |        "type": "string"
          |      }, {
          |        "value": "123",
          |        "type": "number"
          |      }]
          |    }],
          |    "filters": [{
          |       "name": "someFilter",
          |       "args": [{
          |        "value": "World",
          |        "type": "string"
          |      }, {
          |        "value": "321",
          |        "type": "number"
          |      }]
          |    }]
          |  }
          |}""".stripMargin

      val routePatch = routePatchString.parseJson.convertTo[RoutePatch]

      routePatch should be(expected)
    }
  }

  describe("Host") {

    it("should marshall") {
      host.toJson.prettyPrint should be(
        """{
          |  "id": 1,
          |  "name": "name"
          |}""".stripMargin)
    }
  }

  describe("PathIn") {
    it("should unmarshall with default values") {
      val pathIn = PathIn("/hello", Seq(1, 2, 3))

      val result = """{
                     |  "uri": "/hello",
                     |  "host_ids": [1, 2, 3]
                     |}
                   """.stripMargin.parseJson.convertTo[PathIn]

      result should be(pathIn)
    }

    it("should unmarshall with owned_by_team") {
      val pathIn = PathIn("/hello", Seq(1, 2, 3), ownedByTeam = Some(TeamName("other-team")))

      val result = """{
                     |  "uri": "/hello",
                     |  "host_ids": [1, 2, 3],
                     |  "owned_by_team": "other-team"
                     |}
                   """.stripMargin.parseJson.convertTo[PathIn]

      result should be(pathIn)
    }

    it("should unmarshall with has_star") {
      val pathIn = PathIn("/hello", Seq(1, 2, 3), hasStar = Some(true))

      val result = """{
                     |  "uri": "/hello",
                     |  "host_ids": [1, 2, 3],
                     |  "has_star": true
                     |}
                   """.stripMargin.parseJson.convertTo[PathIn]

      result should be(pathIn)
    }

    it("should unmarshall with owned by team") {
      val pathIn = PathIn("/hello", Seq(1, 2, 3), isRegex = Some(true))

      val result = """{
                     |  "uri": "/hello",
                     |  "host_ids": [1, 2, 3],
                     |  "is_regex": true
                     |}
                   """.stripMargin.parseJson.convertTo[PathIn]

      result should be(pathIn)
    }
  }

  describe("PathOut") {

    it("should marshall") {
      pathOut.toJson.prettyPrint should be(
        """{
          |  "created_by": "username",
          |  "owned_by_team": "team",
          |  "host_ids": [1, 2, 3],
          |  "uri": "/hello",
          |  "has_star": false,
          |  "id": 1,
          |  "is_regex": false,
          |  "created_at": "2015-10-10T10:10:10",
          |  "updated_at": "2016-10-10T10:10:10"
          |}""".stripMargin)
    }
  }

  describe("Error") {
    it("should unmarshall the Error") {
      val error = """{ "status": 555, "title": "Error Title", "type": "ERR", "detail": "Error Detail" }""".parseJson.convertTo[Error]
      error.status should be(555)
      error.title should be("Error Title")
      error.errorType should be("ERR")
      error.detail should be(Some("Error Detail"))
    }
  }

  private def host = Host(1, "name")

  private def pathOut = PathOut(
    id = 1,
    uri = "/hello",
    hostIds = Seq(1, 2, 3),
    ownedByTeam = TeamName("team"),
    createdBy = UserName("username"),
    createdAt = LocalDateTime.of(2015, 10, 10, 10, 10, 10),
    updatedAt = LocalDateTime.of(2016, 10, 10, 10, 10, 10),
    hasStar = false,
    isRegex = false
  )
}
