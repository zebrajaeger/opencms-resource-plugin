<%@page buffer="none" session="false" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="cms" type="org.opencms.jsp.util.CmsJspStandardContextBean"--%>
<%--@elvariable id="c" type="org.opencms.jsp.util.CmsJspContentAccessBean"--%>
<%--@elvariable id="v" type="java.util.Map<java.lang.String, org.opencms.jsp.util.CmsJspContentAccessValueWrapper>"--%>
<%--@elvariable id="r" type="java.util.Map<java.lang.String, java.lang.String>"--%>

<cms:formatter var="c" val="v" rdfa="r">
    <c:set var="contentAvailable" value="#[[$]]#{c.hasLocale[cms.locale.language]}"/>

    <c:if test="#[[$]]#{not contentAvailable}">
        <section class="has--missing-translation">
            <p>No content defined for this language</p>
        </section>
    </c:if>

    <c:if test="#[[$]]#{contentAvailable}">
        <div>
            <h2>Hello, i am ${name}</h2>
            <p>Please implement me!</p>
        </div>
    </c:if>
</cms:formatter>
