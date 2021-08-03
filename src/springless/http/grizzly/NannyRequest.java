package springless.http.grizzly;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.http.Cookie;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.Note;
import org.glassfish.grizzly.http.Protocol;
import org.glassfish.grizzly.http.io.InputBuffer;
import org.glassfish.grizzly.http.io.NIOInputStream;
import org.glassfish.grizzly.http.io.NIOReader;
import org.glassfish.grizzly.http.server.AfterServiceListener;
import org.glassfish.grizzly.http.server.HttpServerFilter;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.Session;
import org.glassfish.grizzly.http.server.http2.PushBuilder;
import org.glassfish.grizzly.http.util.Header;
import org.glassfish.grizzly.http.util.Parameters;

import springless.http.querystring.QueryStringBinder;

public class NannyRequest extends Request {
	final Request delegate;

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	@Override
	public void initialize(HttpRequestPacket request, FilterChainContext ctx,
			HttpServerFilter httpServerFilter) {
		delegate.initialize(request, ctx, httpServerFilter);
	}

	@Override
	public HttpRequestPacket getRequest() {
		return delegate.getRequest();
	}

	@Override
	public Response getResponse() {
		return delegate.getResponse();
	}

	@Override
	public String getSessionCookieName() {
		return delegate.getSessionCookieName();
	}

	@Override
	public void setSessionCookieName(String sessionCookieName) {
		delegate.setSessionCookieName(sessionCookieName);
	}

	@Override
	public boolean isPushEnabled() {
		return delegate.isPushEnabled();
	}

	@Override
	public Executor getRequestExecutor() {
		return delegate.getRequestExecutor();
	}

	@Override
	public void addAfterServiceListener(AfterServiceListener listener) {
		delegate.addAfterServiceListener(listener);
	}

	@Override
	public void removeAfterServiceListener(AfterServiceListener listener) {
		delegate.removeAfterServiceListener(listener);
	}

	@Override
	public String getAuthorization() {
		return delegate.getAuthorization();
	}

	@Override
	public PushBuilder newPushBuilder() {
		return delegate.newPushBuilder();
	}

	@Override
	public void replayPayload(Buffer buffer) {
		delegate.replayPayload(buffer);
	}

	@Override
	public NIOInputStream createInputStream() {
		return delegate.createInputStream();
	}

	@Override
	public <E> E getNote(Note<E> note) {
		return delegate.getNote(note);
	}

	@Override
	public Set<String> getNoteNames() {
		return delegate.getNoteNames();
	}

	@Override
	public <E> E removeNote(Note<E> note) {
		return delegate.removeNote(note);
	}

	@Override
	public <E> void setNote(Note<E> note, E value) {
		delegate.setNote(note, value);
	}

	@Override
	public void setServerName(String name) {
		delegate.setServerName(name);
	}

	@Override
	public void setServerPort(int port) {
		delegate.setServerPort(port);
	}

	@Override
	public HttpServerFilter getHttpFilter() {
		return delegate.getHttpFilter();
	}

	@Override
	public String getContextPath() {
		return delegate.getContextPath();
	}

	@Override
	public String getHttpHandlerPath() {
		return delegate.getHttpHandlerPath();
	}

	@Override
	public String getPathInfo() {
		return delegate.getPathInfo();
	}

	@Override
	public Object getAttribute(String name) {
		return delegate.getAttribute(name);
	}

	@Override
	public Set<String> getAttributeNames() {
		return delegate.getAttributeNames();
	}

	@Override
	public String getCharacterEncoding() {
		return delegate.getCharacterEncoding();
	}

	@Override
	public int getContentLength() {
		return delegate.getContentLength();
	}

	@Override
	public long getContentLengthLong() {
		return delegate.getContentLengthLong();
	}

	@Override
	public String getContentType() {
		return delegate.getContentType();
	}

	@Override
	public InputStream getInputStream() {
		return delegate.getInputStream();
	}

	@Override
	public NIOInputStream getNIOInputStream() {
		return delegate.getNIOInputStream();
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean asyncInput() {
		return delegate.asyncInput();
	}

	@Override
	public boolean requiresAcknowledgement() {
		return delegate.requiresAcknowledgement();
	}

	@Override
	public Locale getLocale() {
		return delegate.getLocale();
	}

	@Override
	public List<Locale> getLocales() {
		return delegate.getLocales();
	}

	@Override
	public Parameters getParameters() {
		return delegate.getParameters();
	}

	@Override
	public String getParameter(String name) {
		return delegate.getParameter(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return delegate.getParameterMap();
	}

	@Override
	public Set<String> getParameterNames() {
		return delegate.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		return delegate.getParameterValues(name);
	}

	@Override
	public Protocol getProtocol() {
		return delegate.getProtocol();
	}

	@Override
	public Reader getReader() {
		return delegate.getReader();
	}

	@Override
	public NIOReader getNIOReader() {
		return delegate.getNIOReader();
	}

	@Override
	public String getRemoteAddr() {
		return delegate.getRemoteAddr();
	}

	@Override
	public String getRemoteHost() {
		return delegate.getRemoteHost();
	}

	@Override
	public int getRemotePort() {
		return delegate.getRemotePort();
	}

	@Override
	public String getLocalName() {
		return delegate.getLocalName();
	}

	@Override
	public String getLocalAddr() {
		return delegate.getLocalAddr();
	}

	@Override
	public int getLocalPort() {
		return delegate.getLocalPort();
	}

	@Override
	public String getScheme() {
		return delegate.getScheme();
	}

	@Override
	public String getServerName() {
		return delegate.getServerName();
	}

	@Override
	public int getServerPort() {
		return delegate.getServerPort();
	}

	@Override
	public boolean isSecure() {
		return delegate.isSecure();
	}

	@Override
	public void removeAttribute(String name) {
		delegate.removeAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		delegate.setAttribute(name, value);
	}

	@Override
	public void setCharacterEncoding(String encoding)
			throws UnsupportedEncodingException {
		delegate.setCharacterEncoding(encoding);
	}

	@Override
	public int incrementDispatchDepth() {
		return delegate.incrementDispatchDepth();
	}

	@Override
	public int decrementDispatchDepth() {
		return delegate.decrementDispatchDepth();
	}

	@Override
	public boolean isMaxDispatchDepthReached() {
		return delegate.isMaxDispatchDepthReached();
	}

	@Override
	public void addCookie(Cookie cookie) {
		delegate.addCookie(cookie);
	}

	@Override
	public void addLocale(Locale locale) {
		delegate.addLocale(locale);
	}

	@Override
	public void addParameter(String name, String[] values) {
		delegate.addParameter(name, values);
	}

	@Override
	public void clearCookies() {
		delegate.clearCookies();
	}

	@Override
	public void clearHeaders() {
		delegate.clearHeaders();
	}

	@Override
	public void clearLocales() {
		delegate.clearLocales();
	}

	@Override
	public void clearParameters() {
		delegate.clearParameters();
	}

	@Override
	public String getDecodedRequestURI() throws CharConversionException {
		return delegate.getDecodedRequestURI();
	}

	@Override
	public void setUserPrincipal(Principal principal) {
		delegate.setUserPrincipal(principal);
	}

	@Override
	public String getAuthType() {
		return delegate.getAuthType();
	}

	@Override
	public Cookie[] getCookies() {
		return delegate.getCookies();
	}

	@Override
	public void setCookies(Cookie[] cookies) {
		delegate.setCookies(cookies);
	}

	@Override
	public long getDateHeader(String name) {
		return delegate.getDateHeader(name);
	}

	@Override
	public long getDateHeader(Header header) {
		return delegate.getDateHeader(header);
	}

	@Override
	public String getHeader(String name) {
		return delegate.getHeader(name);
	}

	@Override
	public String getHeader(Header header) {
		return delegate.getHeader(header);
	}

	@Override
	public Iterable<String> getHeaders(String name) {
		return delegate.getHeaders(name);
	}

	@Override
	public Iterable<String> getHeaders(Header header) {
		return delegate.getHeaders(header);
	}

	@Override
	public Map<String, String> getTrailers() {
		return delegate.getTrailers();
	}

	@Override
	public boolean areTrailersAvailable() {
		return delegate.areTrailersAvailable();
	}

	@Override
	public Iterable<String> getHeaderNames() {
		return delegate.getHeaderNames();
	}

	@Override
	public int getIntHeader(String name) {
		return delegate.getIntHeader(name);
	}

	@Override
	public int getIntHeader(Header header) {
		return delegate.getIntHeader(header);
	}

	@Override
	public Method getMethod() {
		return delegate.getMethod();
	}

	@Override
	public void setMethod(String method) {
		delegate.setMethod(method);
	}

	@Override
	public String getQueryString() {
		return delegate.getQueryString();
	}

	@Override
	public void setQueryString(String queryString) {
		delegate.setQueryString(queryString);
	}

	@Override
	public String getRemoteUser() {
		return delegate.getRemoteUser();
	}

	@Override
	public String getRequestedSessionId() {
		return delegate.getRequestedSessionId();
	}

	@Override
	public String getRequestURI() {
		return delegate.getRequestURI();
	}

	@Override
	public void setRequestURI(String uri) {
		delegate.setRequestURI(uri);
	}

	@Override
	public StringBuilder getRequestURL() {
		return delegate.getRequestURL();
	}

	@Override
	public Principal getUserPrincipal() {
		return delegate.getUserPrincipal();
	}

	@Override
	public FilterChainContext getContext() {
		return delegate.getContext();
	}

	@Override
	public InputBuffer getInputBuffer() {
		return delegate.getInputBuffer();
	}

	@Override
	public void setRequestParameters(Parameters parameters) {
		delegate.setRequestParameters(parameters);
	}

	@Override
	public Buffer getPostBody(int len) throws IOException {
		return delegate.getPostBody(len);
	}

	@Override
	public String getJrouteId() {
		return delegate.getJrouteId();
	}

	@Override
	public Session getSession() {
		return delegate.getSession();
	}

	@Override
	public Session getSession(boolean create) {
		return delegate.getSession(create);
	}

	@Override
	public String changeSessionId() {
		return delegate.changeSessionId();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return delegate.isRequestedSessionIdFromCookie();
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return delegate.isRequestedSessionIdFromURL();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return delegate.isRequestedSessionIdValid();
	}

	@Override
	public void setRequestedSessionCookie(boolean flag) {
		delegate.setRequestedSessionCookie(flag);
	}

	@Override
	public void setRequestedSessionId(String id) {
		delegate.setRequestedSessionId(id);
	}

	@Override
	public void setRequestedSessionURL(boolean flag) {
		delegate.setRequestedSessionURL(flag);
	}

	public NannyRequest(Request origin, final Response response) {
		super(response);
		this.delegate = origin;
	}

	public void bindParameter(Object p) throws Exception {
		final QueryStringBinder binder = new QueryStringBinder();
		binder.bind(this.delegate, p);
	}
}
