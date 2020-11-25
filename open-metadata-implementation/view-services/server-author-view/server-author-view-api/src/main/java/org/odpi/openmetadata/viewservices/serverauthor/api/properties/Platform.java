/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.serverauthor.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Platform {

    private static final long    serialVersionUID = 1L;


    private String platformName;
    private String platformDescription;
    private PlatformStatus platformStatus;
    private Set<StoredServer> storedServers = new HashSet<>();
    /**
     * Default Constructor sets the properties to nulls
     */
    public Platform()
    {
        /*
         * Nothing to do.
         */
    }

    /**
     * Constructor

     * @param platformName platform name
     * @param platformDescription platform description
     */
    public Platform(String platformName, String platformDescription) {

        this.platformName        = platformName;
        this.platformDescription = platformDescription;
    }


    /**
     * Get the platform name
     * @return platform name
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * Set a meaningful name for the platform
     * @param platformName set platform name
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    /**
     * Get the platform description
     * @return platform description
     */
    public String getPlatformDescription() {
        return platformDescription;
    }

    /**
     * Set the description for the platform
     * @param platformDescription set platform description
     */
    public void setPlatformDescription(String platformDescription) {
        this.platformDescription = platformDescription;
    }

    /**
     * Set the stored servers on this platform
     * @return the stored servers on this platform
     */
    public Set<StoredServer> getStoredServers() {
        return storedServers;
    }

    /**
     * Get the stored servers on this platform
     * @param storedServers servers
     */
    public void setStoredServers(Set<StoredServer> storedServers) {
        this.storedServers = storedServers;
    }
    /**
     * Add a stored server to the platform
     * @param storedServer stored server to add
     */
    public void addStoredServer(StoredServer storedServer) {
        storedServers.add(storedServer);
    }

    /**
     * return the status of the platform
     * @return the returned platform status
     */
    public PlatformStatus getPlatformStatus() {
        return platformStatus;
    }

    /**
     * Set the platform status
     * @param platformStatus platform status to set
     */
    public void setPlatformStatus(PlatformStatus platformStatus) {
        this.platformStatus = platformStatus;
    }
}
