/*-
 * <<
 * wormhole
 * ==
 * Copyright (C) 2016 - 2017 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */


package edp.rider.rest.router.admin.routes

import javax.ws.rs.Path

import akka.http.scaladsl.server.{Directives, Route}
import edp.rider.module._
import edp.rider.rest.persistence.entities.{ProjectUserNs, SimpleProjectRel}
import edp.rider.rest.router.JsonProtocol._
import edp.rider.rest.router.SessionClass
import edp.rider.rest.util.AuthorizationProvider
import io.swagger.annotations._

@Api(value = "/projects", consumes = "application/json", produces = "application/json")
@Path("/admin/projects")
class ProjectAdminRoutes(modules: ConfigurationModule with PersistenceModule with BusinessModule with RoutesModuleImpl) extends Directives {

  lazy val routes: Route = getProjectByIdRoute ~ getProjectByFilterRoute ~ postProjectRoute ~ putProjectRoute ~
    getUserByProjectIdRoute ~ getUserByProjectRoute ~ getNsByProjectIdRoute ~ getNsByProjectRoute ~ getFlowByProjectIdRoute ~
    getStreamByProjectIdRoute ~ getResourceByProjectIdRoute ~ getLogByStreamId ~ getMonitorDashboardRoute ~ getTopicsByStreamId ~
    deleteProjectByIdRoute ~ getNonPublicUdfByProjectRoute

  lazy val basePath = "projects"

  @Path("/{id}")
  @ApiOperation(value = "get one project from system by id", notes = "", nickname = "", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "project id", required = true, dataType = "integer", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getProjectByIdRoute: Route = modules.projectAdminService.getByIdRoute(basePath)

  @ApiOperation(value = "get all projects", notes = "", nickname = "", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "visible", value = "true or false", required = false, dataType = "boolean", paramType = "query", allowMultiple = true),
    new ApiImplicitParam(name = "name", value = "project name", required = false, dataType = "string", paramType = "query", allowMultiple = true)
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 501, message = "the request url is not supported"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getProjectByFilterRoute: Route = modules.projectAdminService.getByFilterRoute(basePath)

  @Path("/{id}/users")
  @ApiOperation(value = "get one project's users selected information from system by id", notes = "", nickname = "", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "project id", required = true, dataType = "integer", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getUserByProjectIdRoute: Route = modules.userAdminService.getByProjectIdRoute(basePath)

  @Path("/users")
  @ApiOperation(value = "get all users", notes = "", nickname = "", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getUserByProjectRoute: Route = modules.userAdminService.getNormalUserRoute(basePath)

  @Path("/{id}/namespaces")
  @ApiOperation(value = "get one project's namespaces selected information from system by id", notes = "", nickname = "", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "project id", required = true, dataType = "integer", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getNsByProjectIdRoute: Route = modules.namespaceAdminService.getByProjectIdRoute(basePath)

  @Path("/namespaces")
  @ApiOperation(value = "get all namespaces", notes = "", nickname = "", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getNsByProjectRoute: Route = modules.namespaceAdminService.getNsByProjectRoute(basePath)

  @Path("/udfs")
  @ApiOperation(value = "get all non public udfs", notes = "", nickname = "", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getNonPublicUdfByProjectRoute: Route = modules.udfAdminService.getNonPublicUdfRoute(basePath)

  @Path("/{id}/flows")
  @ApiOperation(value = "get one project's flows from system by id", notes = "", nickname = "", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "project id", required = true, dataType = "integer", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getFlowByProjectIdRoute: Route = modules.flowAdminService.getByProjectIdRoute(basePath)

  @Path("/{id}/streams")
  @ApiOperation(value = "get one project's streams from system by id", notes = "", nickname = "", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "project id", required = true, dataType = "integer", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getStreamByProjectIdRoute: Route = modules.streamAdminService.getByProjectIdRoute(basePath)

  @Path("/{id}/resources")
  @ApiOperation(value = "get one project's resource information from system by id", notes = "", nickname = "", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "project id", required = true, dataType = "integer", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getResourceByProjectIdRoute: Route = modules.streamAdminService.getResourceByProjectIdRoute(basePath)

  @Path("/{id}/streams/{streamId}/logs/")
  @ApiOperation(value = "get stream log by stream id", notes = "", nickname = "", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "project id", required = true, dataType = "integer", paramType = "path"),
    new ApiImplicitParam(name = "streamId", value = "stream id", required = true, dataType = "integer", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getLogByStreamId: Route = modules.streamAdminService.getLogByStreamId(basePath)


  @ApiOperation(value = "Add new project to the system", notes = "", nickname = "", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "project", value = "Project object to be added", required = true, dataType = "edp.rider.rest.persistence.entities.SimpleProjectRel", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "post success"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 409, message = "project already exists"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def postProjectRoute: Route = modules.projectAdminService.postRoute(basePath)


  @ApiOperation(value = "update project in the system", notes = "", nickname = "", httpMethod = "PUT")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "project", value = "Project object to be updated", required = true, dataType = "edp.rider.rest.persistence.entities.ProjectUserNs", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "put success"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def putProjectRoute: Route = modules.projectAdminService.putRoute(basePath)

  @Path("/{id}/monitors")
  @ApiOperation(value = "get one project's resource information from system by id", notes = "", nickname = "", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "project id", required = true, dataType = "integer", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getMonitorDashboardRoute: Route = modules.monitorAdminService.getDashboardByProjectIdRoute(basePath)

  @Path("/{id}/streams/{streamId}/intopics/")
  @ApiOperation(value = "get one stream topics by stream id", notes = "", nickname = "", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "project id", required = true, dataType = "integer", paramType = "path"),
    new ApiImplicitParam(name = "streamId", value = "stream id", required = true, dataType = "integer", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not normal"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def getTopicsByStreamId: Route = modules.streamAdminService.getTopicsByStreamId(basePath)

  @Path("/{id}/")
  @ApiOperation(value = "delete one project from system by id", notes = "", nickname = "", httpMethod = "DELETE")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "project id", required = true, dataType = "integer", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 401, message = "authorization error"),
    new ApiResponse(code = 403, message = "user is not admin user"),
    new ApiResponse(code = 412, message = "project still has some streams"),
    new ApiResponse(code = 451, message = "request process failed"),
    new ApiResponse(code = 500, message = "internal server error")
  ))
  def deleteProjectByIdRoute: Route = modules.projectAdminService.deleteRoute(basePath)
}

