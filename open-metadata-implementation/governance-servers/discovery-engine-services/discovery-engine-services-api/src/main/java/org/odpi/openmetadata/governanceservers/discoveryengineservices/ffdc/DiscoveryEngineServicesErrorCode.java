/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.discoveryengineservices.ffdc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ODF error code is used to define first failure data capture (FFDC) for errors that occur when working with
 * ODF Discovery Services.  It is used in conjunction with all ODF Exceptions, both Checked and Runtime (unchecked).
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DiscoveryEngineServicesErrorCode
{
    /*
     * Invalid configuration document - these errors need the server to be restarted to resolve.
     */
    NO_CONFIG_DOC(400,"DISCOVERY-ENGINE-SERVICES-400-001 ",
                  "Discovery server {0} has been passed a null configuration document section for the discovery engine services",
                  "The discovery engine services can not retrieve its configuration values.  " +
                          "The hosting discovery server fails to start.",
                  "This is an internal logic error since the admin services should not have initialized the discovery engine services" +
                          "without this section of the configuration document filled in.  Raise an issue to get this fixed."),

    NO_OMAS_SERVER_URL(400,"DISCOVERY-ENGINE-SERVICES-400-002 ",
                       "Discovery server {0} is not configured with the platform URL root for the Discovery Engine OMAS",
                       "The discovery engine services is not able to locate the metadata server to retrieve the configuration for " +
                               "the discovery engines.  The discovery server fails to start.",
                       "To be successful the discovery engine services needs both the platform URL root and the name of the metadata " +
                               "server as well as the list of discovery engines it is to host. Add the " +
                               "configuration for the platform URL root to this discovery server's configuration document and check that the " +
                               "other required configuration properties are in place. Then restart the discovery server."),

    NO_OMAS_SERVER_NAME(400, "DISCOVERY-ENGINE-SERVICES-400-003 ",
                        "Discovery server {0} is not configured with the name for the server running the Discovery Engine OMAS",
                        "The server is not able to retrieve its configuration from the metadata server.  It fails to start.",
                        "Add the configuration for the server name to this discovery server's configuration document.  " +
                                "Ensure that the platform URL root points to the platform where the metadata server is running and that" +
                                "there is at least one discovery engine listed.  Once the configuration document is set up correctly,  " +
                                "restart the discovery server."),

    NO_DISCOVERY_ENGINES(400,"DISCOVERY-ENGINE-SERVICES-400-004 ",
                         "Discovery server {0} is not configured with any discovery engines",
                         "The server is not able to run any discovery requests.  It fails to start.",
                         "Add the qualified name for at least one discovery engine to the discovery engine services section" +
                                 "of this discovery server's configuration document " +
                                 "and then restart the discovery server."),

    DISCOVERY_ENGINE_INSTANCE_FAILURE(400, "DISCOVERY-ENGINE-SERVICES-400-007 ",
                         "The discovery engine services are unable to initialize a new instance of discovery engine {0}; error message is {1}",
                         "The discovery engine services detected an error during the start up of a specific discovery engine instance.  " +
                                              "Its discovery services are not available in the discovery server.",
                         "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                              "Once this is resolved, restart the server."),

    SERVICE_INSTANCE_FAILURE(400, "DISCOVERY-ENGINE-SERVICES-400-008 ",
                             "The discovery engine services are unable to initialize a new instance of discovery server {0}; error message is {1}",
                             "The discovery engine services detected an error during the start up of a specific discovery server instance.  " +
                                     "No discovery services are available in the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server."),

    /*
     * Unavailable configuration in metadata server (the server may be down or the definitions are not loaded in the metadata server).
     * These errors are returned to the caller while the discovery server is retrying its attempts to retrieve the configuration.
     * The problem is transient - once the configuration is available in the metadata server and the discovery server has retrieved the
     * configuration, the discovery engines will operate successfully.
     */
    CONFIGURATION_LISTENER_INSTANCE_FAILURE(400, "DISCOVERY-ENGINE-SERVICES-400-010 ",
             "The discovery engine services are unable to retrieve the connection for the configuration " +
                                  "listener for discovery server {0} from metadata server {1}. " +
                                  "Exception returned was {2} with error message {3}",
             "The discovery server continues to run.  The discovery engine services will start up the " +
                                  "discovery engines and they will operate with whatever configuration that they can retrieve.  " +
                                  "Periodically the discovery engine services will" +
                                  "retry the request to retrieve the connection information.  " +
                                  "Without the connection, the discovery server will not be notified of changes to the discovery " +
                                                    "engines' configuration",
              "This problem may be caused because the discovery server has been configured with the wrong location for the " +
                                  "metadata server, or the metadata server is not running the Discovery Engine OMAS service or " +
                                  "the metadata server is not running at all.  Investigate the status of the metadata server to " +
                                  "ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the " +
                                  "refresh-config command or wait for the discovery server to retry the configuration request."),

    UNKNOWN_DISCOVERY_ENGINE_CONFIG_AT_STARTUP(400, "DISCOVERY-ENGINE-SERVICES-400-011 ",
             "Properties for discovery engine called {0} have not been returned by open metadata server {1}.  Exception {2} " +
                                            "with message {3} returned to discovery server {4}",
             "The discovery server is not able to initialize the discovery engine and so it will not de able to support discovery " +
                                 "requests targeted to this discovery engine.  ",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the discovery server.  Once the cause is resolved, restart the discovery server."),

    NO_DISCOVERY_ENGINES_STARTED(400,"DISCOVERY-ENGINE-SERVICES-400-012 ",
             "Discovery server {0} is unable to start any discovery engines",
             "The server is not able to run any discovery requests.  It fails to start.",
             "Add the configuration for at least one discovery engine to this discovery server."),

    NO_DISCOVERY_ENGINE_CLIENT(400,"DISCOVERY-ENGINE-SERVICES-400-013 ",
                                 "Discovery server {0} is unable to start a client to the Discovery Engine OMAS for discovery engine {1}.  The " +
                                         "exception was {2} and the error message was {3}",
                                 "The server is not able to run any discovery requests.  It fails to start.",
                                 "Using the information in the error message, correct the server configuration and restart the server."),


    UNKNOWN_DISCOVERY_ENGINE_CONFIG(400, "DISCOVERY-ENGINE-SERVICES-400-014 ",
             "Properties for discovery engine called {0} have not been returned by open metadata server {1} to discovery server {2}",
             "The discovery server is not able to initialize the discovery engine and so it will not de able to support discovery " +
                                            "requests targeted to this discovery engine.",
             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                    "configuration of the discovery server.  Once the cause is resolved, restart the discovery server."),

    /*
     * Errors when running requests
     */
    UNKNOWN_DISCOVERY_ENGINE(400, "DISCOVERY-ENGINE-SERVICES-400-020 ",
                             "Discovery engine {0} is not running in the discovery server {1}",
                             "The discovery engine requested on a request is not known to the discovery server.",
                             "This may be a configuration error in the discovery server or an error in the caller.  " +
                                     "The supported discovery engines are listed in the discovery server's configuration.  " +
                                     "Check the configuration document for the server and then its start up messages to ensure the correct " +
                                     "discovery engines are started.  Look for other error messages that indicate that an error occurred during " +
                                     "start up.  If the discovery server is running the correct discovery engines then validate that " +
                                     "the caller has passed the correct name of the discovery engine to the discovery server.  If all of this is " +
                                     "correct then it may be a code error in the discovery engine services and you need to raise an issue to get " +
                                     "it fixed.  Once the cause is resolved, retry the discovery request."),

    UNKNOWN_DISCOVERY_REQUEST_TYPE(400, "DISCOVERY-ENGINE-SERVICES-400-021 ",
             "The discovery request type {0} is not recognized by discovery engine {1} hosted by discovery server {2}",
             "The discovery request is not run and an error is returned to the caller.",
             "This may be an error in the caller's logic, a configuration error related to the discovery engine or the metadata server" +
                                           "used by the discovery server may be down.  " +
                                           "The configuration that defines the discovery request type in the discovery engine and links " +
                                           "it to the discovery service that should run is maintained in the metadata server by the Discovery " +
                                           "Engine OMAS's configuration API." +
                                           "Verify that this configuration is correct, that the metadata server is running and the discovery " +
                                           "server has been able to retrieve the configuration.  If all this is true and the caller's request is " +
                                           "consistent with this configuration then it may be a code error in the discovery server in which case, " +
                                           "raise an issue to get it fixed.  Once the cause is resolved, retry the discovery request."),

    INVALID_DISCOVERY_SERVICE(400, "DISCOVERY-ENGINE-SERVICES-400-022 ",
             "The discovery service {0} linked to discovery request type {1} can not be started.  " +
                     "The {2} exception was returned with message {3}",
             "The discovery request is not run and an error is returned to the caller.",
             "This may be an error in the discovery services's logic or the discovery service may not be properly deployed or " +
                                      "there is a configuration error related to the discovery engine.  " +
                                      "The configuration that defines the discovery request type in the discovery engine and links " +
                                      "it to the discovery service is maintained in the metadata server by the Discovery " +
                                      "Engine OMAS's configuration API." +
                                      "Verify that this configuration is correct.  If it is then validate that the jar file containing the " +
                                      "discovery service's implementation has been deployed so the discovery server can load it.  If all this is " +
                                      "true this it is likely to be a code error in the discovery service in which case, " +
                                      "raise an issue with the author of the discovery service to get it fixed.  Once the cause is resolved, " +
                                      "retry the discovery request."),

    NULL_DISCOVERY_SERVICE(400, "DISCOVERY-ENGINE-SERVICES-400-023 ",
                              "Method {0} can not execute in the discovery engine {1} hosted by discovery server {2} because the associated " +
                                      "discovery service properties are null",
                              "The discovery request is not run and an error is returned to the caller.",
                              "This may be an error in the discovery engine's logic or the Discovery Engine OMAS may have returned " +
                                   "invalid configuration.  Raise an issue to get help to fix it"),

    DISCOVERY_ENGINE_NOT_INITIALIZED(400,"DISCOVERY-ENGINE-SERVICES-400-024 ",
             "Discovery server {0} is unable to pass a discovery request to discovery engine {1} because this discovery engine has not " +
                                             "retrieved its configuration from the metadata server",
                                     "The discovery engine is not able to run any discovery requests until it is able to retrieve its configuration.",
                                     "."),
    ;


    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(DiscoveryEngineServicesErrorCode.class);


    /**
     * The constructor for DiscoveryEngineServicesErrorCode expects to be passed one of the enumeration rows defined in
     * DiscoveryEngineServicesErrorCode above.   For example:
     *
     *     DiscoveryEngineServicesErrorCode   errorCode = DiscoveryEngineServicesErrorCode.UNKNOWN_ENDPOINT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    DiscoveryEngineServicesErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    public int getHTTPErrorCode()
    {
        return httpErrorCode;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    public String getErrorMessageId()
    {
        return errorMessageId;
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params   strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("DiscoveryEngineServicesErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction()
    {
        return userAction;
    }
}
