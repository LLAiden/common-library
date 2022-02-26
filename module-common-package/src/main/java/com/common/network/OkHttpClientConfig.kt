package com.common.network

import okhttp3.*
import okhttp3.internal.asFactory
import okhttp3.internal.connection.RouteDatabase
import okhttp3.internal.immutableListOf
import okhttp3.internal.tls.CertificateChainCleaner
import okhttp3.internal.tls.OkHostnameVerifier
import okhttp3.internal.ws.RealWebSocket
import java.net.Proxy
import java.net.ProxySelector
import javax.net.SocketFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

class OkHttpClientConfig {
     var dispatcher: Dispatcher = Dispatcher()
     var connectionPool: ConnectionPool = ConnectionPool()
     val interceptors: MutableList<Interceptor> = mutableListOf()
     val networkInterceptors: MutableList<Interceptor> = mutableListOf()
     var eventListenerFactory: EventListener.Factory = EventListener.NONE.asFactory()
     var retryOnConnectionFailure = true
     var authenticator: Authenticator = Authenticator.NONE
     var followRedirects = true
     var followSslRedirects = true
     var cookieJar: CookieJar = CookieJar.NO_COOKIES
     var cache: Cache? = null
     var dns: Dns = Dns.SYSTEM
     var proxy: Proxy? = null
     var proxySelector: ProxySelector? = null
     var proxyAuthenticator: Authenticator = Authenticator.NONE
     var socketFactory: SocketFactory = SocketFactory.getDefault()
     var sslSocketFactoryOrNull: SSLSocketFactory? = null
     var x509TrustManagerOrNull: X509TrustManager? = null
     var connectionSpecs: List<ConnectionSpec> = immutableListOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT)
     var protocols: List<Protocol> = immutableListOf(Protocol.HTTP_2, Protocol.HTTP_1_1)
     var hostnameVerifier: HostnameVerifier = OkHostnameVerifier
     var certificatePinner: CertificatePinner = CertificatePinner.DEFAULT
     var certificateChainCleaner: CertificateChainCleaner? = null
     var callTimeout = 0
     var connectTimeout = 10_000
     var readTimeout = 10_000
     var writeTimeout = 10_000
     var pingInterval = 0
     var minWebSocketMessageToCompress = RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE
     var routeDatabase: RouteDatabase? = null
}