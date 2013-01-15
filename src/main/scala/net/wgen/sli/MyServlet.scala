package net.wgen.sli

import dispatch.Http
import org.scalatra._
import scalate.ScalateSupport
import org.slf4j.LoggerFactory
import org.apache.http.{ NameValuePair, HttpResponse, HttpEntity }
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{ HttpPost, HttpGet }
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.protocol.HTTP
import java.io.InputStreamReader
import java.io.StringWriter
import org.apache.http.util.EntityUtils
import com.codahale.jerkson.Json._
class MyServlet extends ScalatraServlet with ScalateSupport {

  val logger = LoggerFactory.getLogger(getClass)

  val apiuri = "https://api.sandbox.slcedu.org/api"
  val clientid = "" //Add your ID here.
  val clientsecret = "" // Add your client secret here
  
  val callbackuri = "http://localhost:8081/sample/callback"

  get("/home/") {

    if (!session.contains("token")) {
      redirect("/sample")
    }
    val token = session("token")

    val homeUri = apiuri + "/rest/v1/home"
    val httpclient = new DefaultHttpClient()
    val httpget = new HttpGet(homeUri)

    logger.info("Token from /home: " + token)
    logger.info("Request URI: " + homeUri)

    httpget.addHeader("Authorization", "Bearer " + token)
    val response = httpclient.execute(httpget)
    val entity = response.getEntity()

    val content = EntityUtils.toString(entity)

    logger.info("Home content:" + content)

  

    contentType = "text/html"

    layoutTemplate("/WEB-INF/layouts/home.ssp", "title" -> "Home", "body" -> content)
  }

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say<a href="hello-scalate">hello to Scalate</a>
        .
      </body>
    </html>
  }

  get("/sample/?") {

    val loginUri = apiuri + "/oauth/authorize?Realm=SandboxIDP&response_type=code&client_id=" + clientid + "&redirect_uri=" + callbackuri
    logger.info("LoginUri: " + loginUri)
    redirect(loginUri)

  }
  get("/sample/callback/?") {

    logger.info("Callback: " + request)
    logger.info("Params: " + params)
    val code = params("code")

    val tokenUri = apiuri + "/oauth/token?Realm=SandboxIDP&code=" + code + "&client_id=" + clientid + "&redirect_uri=" + callbackuri + "&client_secret=" + clientsecret

    logger.info("Token URI: " + tokenUri)

    val token = getToken(tokenUri)

    session("token") = token
    redirect("/home/")

  }

  notFound {
    // remove content type in case it was set through an action
    contentType = null
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }

  def getToken(tokenUri: String): String = {

    val svc = url(tokenUri)

    val httpclient = new DefaultHttpClient()
    val httpget = new HttpGet(tokenUri)
    val response = httpclient.execute(httpget)
    val entity = response.getEntity()

    val content = EntityUtils.toString(entity)
    val responseMap = parse[Map[String, String]](content)
    val accessToken = responseMap("access_token")

    logger.info("Response: " + response)
    logger.info("Entity:" + entity)
    logger.info("Content: " + content)
    logger.info("Response map: " + responseMap)
    logger.info("Access token: " + accessToken)

    return accessToken
  }
}
