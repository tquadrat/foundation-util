/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tquadrat.foundation.util;

import static java.lang.Math.max;
import static java.lang.System.arraycopy;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperties;
import static java.lang.System.getProperty;
import static java.lang.System.nanoTime;
import static java.lang.Thread.currentThread;
import static java.net.InetAddress.getByName;
import static java.util.Arrays.stream;
import static java.util.Collections.list;
import static java.util.Locale.ROOT;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.apiguardian.api.API.Status.DEPRECATED;
import static org.apiguardian.api.API.Status.EXPERIMENTAL;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.PROPERTY_CPUARCHITECTURE;
import static org.tquadrat.foundation.lang.CommonConstants.PROPERTY_OSNAME;
import static org.tquadrat.foundation.lang.CommonConstants.PROPERTY_OSVERSION;
import static org.tquadrat.foundation.lang.CommonConstants.TIME_DELTA_BEGINGREGORIAN2BEGINEPOCH;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.splitString;

import java.io.File;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.IllformedLocaleException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.lang.Lazy;
import org.tquadrat.foundation.lang.SoftLazy;

/**
 *  This class provides some system related helper and convenience
 *  methods.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SystemUtils.java 1021 2022-03-01 22:53:02Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"ClassWithTooManyMethods", "OverlyComplexClass"} )
@ClassVersion( sourceVersion = "$Id: SystemUtils.java 1021 2022-03-01 22:53:02Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
@UtilityClass
public final class SystemUtils
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The operating system families that are supported (or not) by Java.<br>
     *  <br>Currently, we can distinguish only between Microsoft Windows,
     *  UNIX/Linux and MacOX/OS-X.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: SystemUtils.java 1021 2022-03-01 22:53:02Z tquadrat $
     *  @since 0.0.6
     *
     *  @UMLGraph.link
     *
     *  @see SystemUtils#determineOperatingSystem()
     */
    @ClassVersion( sourceVersion = "$Id: SystemUtils.java 1021 2022-03-01 22:53:02Z tquadrat $" )
    @API( status = STABLE, since = "0.0.6" )
    public static enum OperatingSystem
    {
            /*------------------*\
        ====** Enum Declaration **=============================================
            \*------------------*/
        /**
         *  The operating system is OS-X
         */
        OSX,

        /**
         *  The operating system is UNIX/Linux.
         */
        UNIX,

        /**
         *  The operating system is Microsoft Windows.
         */
        WINDOWS,

        /**
         *  The current operating system is unknown to this library.
         */
        UNKNOWN;

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Returns the operating system architecture.
         *
         *  @return The architecture.
         */
        @SuppressWarnings( "static-method" )
        public final String getArchitecture() {return getProperty( PROPERTY_CPUARCHITECTURE ); }

        /**
         *  Returns the name of the operating system.
         *
         *  @return The operating system name.
         */
        @SuppressWarnings( "static-method" )
        public final String getName() { return getProperty( PROPERTY_OSNAME ); }

        /**
         *  Returns the name of the operating system.
         *
         *  @return The operating system name.
         */
        public final String getNameVersion() { return format( "%s %s (%s)", getName(), getVersion(), getArchitecture() ); }

        /**
         *  Returns the version of the operating system.
         *
         *  @return The version of the operating system.
         */
        @SuppressWarnings( "static-method" )
        public final String getVersion() { return getProperty( PROPERTY_OSVERSION ); }
    }
    //  enum OperatingSystem

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The length of a String containing a MAC address: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int MAC_ADDRESS_Size = 17;

    /**
     *  The regular expression for a valid IPv4 address: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String PATTERN_IPv4_ADDRESS = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The location or node id.
     */
    private static Long m_Node = null;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The adjustment for the values that are returned from
     *  {@link System#nanoTime()}.
     */
    private static final BigInteger m_NanoAdjust;

    /**
     *  The operating system.
     */
    private static final Lazy<OperatingSystem> m_OperatingSystem;

    /**
     *  The pattern to check a string for an IPv4 address.
     */
    private static final Lazy<Pattern> m_PatternIPv4Address;
    /**
     *  The random number generator.
     */
    private static final Lazy<Random> m_Random;

    /**
     *  The alias map.
     *
     *  @see #createZoneIdAliasMap()
     */
    private static final SoftLazy<Map<String,String>> m_ZoneIdAliasMap;

    static
    {
        //---* The operating system *------------------------------------------
        m_OperatingSystem = Lazy.use( SystemUtils::determineOperatingSystem );

        //---* The pattern for the IP addresses *------------------------------
        m_PatternIPv4Address = Lazy.use( () ->
        {
            try
            {
                return Pattern.compile( PATTERN_IPv4_ADDRESS );
            }
            catch( final PatternSyntaxException e )
            {
                throw new ExceptionInInitializerError( e );
            }
        } );

        //---* The random number generator *-----------------------------------
        m_Random = Lazy.use( Random::new );

        //---* The time adjustment *-------------------------------------------
        final var thousand = BigInteger.valueOf( 1000 );
        final var timeDelta = BigInteger.valueOf( TIME_DELTA_BEGINGREGORIAN2BEGINEPOCH ).multiply( thousand ).multiply( thousand );
        final var nanoNow = BigInteger.valueOf( nanoTime() );
        final var milliNow = BigInteger.valueOf( currentTimeMillis() ).multiply( thousand );
        m_NanoAdjust = milliNow.subtract( nanoNow ).add( timeDelta );

        //---* Initialise the ZoneId Alias Map *-------------------------------
        m_ZoneIdAliasMap = SoftLazy.use( SystemUtils::createZoneIdAliasMap );
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private SystemUtils() { throw new PrivateConstructorForStaticClassCalledError( SystemUtils.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Converts an IP address that is given as a string into an
     *  {@link InetAddress}
     *  object.
     *
     *  TODO Currently this method do not process IPv6 addresses properly.
     *
     *  @param  ipAddress   The string with the IP address.
     *  @return The {@code InetAddress} object.
     *  @throws IllegalArgumentException    The given parameter is not a valid
     *      IP address.
     */
    @API( status = EXPERIMENTAL, since = "0.0.5" )
    public static final InetAddress convertIPAddress( final CharSequence ipAddress )
    {
        final var message = "IP address is invalid: %1$s";
        InetAddress retValue = null;

        final var arg = requireNotEmptyArgument( ipAddress, "ipAddress" ).toString();
        if( m_PatternIPv4Address.get().matcher( arg ).matches() || arg.contains( ":" ) )
        {
            try
            {
                retValue = getByName( arg );
            }
            catch( final UnknownHostException e )
            {
                throw new IllegalArgumentException( format( message, arg ), e );
            }
        }
        else
        {
            throw new IllegalArgumentException( format( message, arg ) );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  convertIPAddress()

    /**
     *  Returns a pseudo node id; this is useful in cases a machine does not
     *  have a network card (NIC) so that a MAC address could not be obtained,
     *  or if the real node id should not be used.<br>
     *  <br>Each call to this method will return a new value.
     *
     *  @return The node id.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final long createPseudoNodeId()
    {
        final var retValue = (getRandom().nextLong() & 0x0000FFFFFFFFFFFFL) | 0x0000010000000000L;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createPseudoNodeId()

    /**
     *  Creates the alias map for the old (deprecated) zone ids that is used
     *  for the call to
     *  {@link ZoneId#of(String, java.util.Map)}
     *  to retrieve a
     *  {@link ZoneId}
     *  instance for the given zone id.
     *
     *  @return The alias map.
     *
     *  @since 0.0.6
     */
    @API( status = STABLE, since = "0.0.6" )
    public static final Map<String,String> createZoneIdAliasMap()
    {
        final var retValue = stream( TimeZone.getAvailableIDs() )
            .filter( id -> !ZoneId.getAvailableZoneIds().contains( id ) )
            .collect( toMap( identity(), id -> TimeZone.getTimeZone( id ).toZoneId().normalized().toString() ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createZoneIdAliasMap()

    /**
     *  Returns the current time as nanoseconds since the beginning of the
     *  Gregorian calendar (1582-10-15T00:00).
     *
     *  @return The current time in nanoseconds.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final BigInteger currentTimeNanos()
    {
        final var retValue = m_NanoAdjust.add( BigInteger.valueOf( nanoTime() ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  currentTimeNanos()

    /**
     *  Determines an IP address of the machine this program is running on.
     *  Usually this will be one of the addresses that is visible to the
     *  outside. In addition, the method has a clear precedence for IPv4
     *  addresses over IPv6 ones.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the IP address; it will be empty if really no network
     *      adapter is active on this machine.
     *  @throws SocketException Problems to retrieve the internet address.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Optional<InetAddress> determineIPAddress() throws SocketException
    {
        Optional<InetAddress> retValue = Optional.empty();

        List<InetAddress> addresses;
        ScanLoop: for( final var i : getNetworkInterfaces() )
        {
            /*
             * Due to the nature of the scan logic, the returned address will
             * always be that of the last NIC in sequence that is not the
             * loopback interface.
             */
            if( i.isUp() )
            {
                addresses = list( i.getInetAddresses() );
                AddressLoop: for( final var address : addresses )
                {
                    /*
                     * The AddressLoop terminates early, when the first IPv4
                     * address for the NIC was found.
                     */
                    if( i.isLoopback() )
                    {
                        /*
                         * We take {@code loopback} if we do not get
                         * something better ...
                         */
                        if( retValue.isEmpty() )
                        {
                            retValue = Optional.of( address );
                            if( address instanceof Inet4Address ) break AddressLoop;
                        }
                        else if( !(retValue.get() instanceof Inet4Address) )
                        {
                            retValue = Optional.of( address );
                            break AddressLoop;
                        }
                    }
                    else
                    {
                        retValue = Optional.of( address );
                        if( address instanceof Inet4Address ) break AddressLoop;
                    }
                }
            }
        }   //  ScanLoop:

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineIPAddress()

    /**
     *  Determines all the IP addresses of the machine this program is running
     *  on.
     *
     *  @return The IP addresses.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final InetAddress [] determineIPAddresses()
    {
        final var retValue = getNetworkInterfaces().stream()
            .flatMap( i -> list( i.getInetAddresses() ).stream() )
            .toArray( InetAddress []::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineIPAddresses()

    /**
     *  Determines the operating system.
     *
     *  @return The operating system.
     */
    private static final OperatingSystem determineOperatingSystem()
    {
        /*
         * TODO Add more criteria for each family. And perhaps it should be
         * distinguished between Linux, BSD and Unix (and there between HP-UX
         * and AIX) ...
         */
        var retValue = OperatingSystem.UNKNOWN;

        /*
         * All currently known MS-DOS and Windows versions and their file
         * systems use the backslash as the separator for folders.
         * They also use the semicolon as the PATH separator.
         */
        final var fileSeparator = File.separatorChar;
        final var pathSeparator = File.pathSeparatorChar;
        if( (fileSeparator == '\\') && (pathSeparator == ';') )
        {
            retValue = OperatingSystem.WINDOWS;
        }
        else if( (fileSeparator == '/') && (pathSeparator == ':') )
        {
            if( getProperty( PROPERTY_OSNAME ).toLowerCase( ROOT ).contains( "osx" ) )
            {
                retValue = OperatingSystem.OSX;
            }
            else
            {
                retValue = OperatingSystem.UNIX;
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineOperatingSystem()

    /**
     *  Determines those IP addresses of the machine this program is running
     *  on that are used to communicate with the outside world. This means that
     *  only those <i>active</i> network interfaces are considered that are
     *  <i>not</i> a loopback interface.
     *
     *  @return The IP addresses.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final InetAddress [] determineOutboundIPAddresses()
    {
        final var retValue = getNetworkInterfaces().stream()
            .filter( i ->
            {
                try
                {
                    return i.isUp() && !i.isLoopback();
                }
                catch( @SuppressWarnings( "unused" ) final SocketException e )
                {
                    return false;
                }
            } )
            .flatMap( i -> list( i.getInetAddresses() ).stream() )
            .toArray( InetAddress []::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineOutboundIPAddresses()

    /**
     *  Determines those IP addresses of the machine this program is running
     *  on that are used to communicate with the outside world. This means that
     *  only those <i>active</i> network interfaces are considered that are
     *  <i>not</i> a loopback interface.
     *
     *  @return The IP addresses.
     *
     *  @deprecated Use
     *      {@link #determineOutboundIPAddresses()}
     *      instead.
     */
    @API( status = DEPRECATED )
    @Deprecated( forRemoval = true )
    public static InetAddress [] determineOutsideIPAddresses()
    {
        return determineOutboundIPAddresses();
    }   //  determineOutsideIPAddresses()

    /**
     *  Formats the given node id as a MAC address string.
     *
     *  @param  nodeId  The node id.
     *  @return The MAC address string.
     */
    @SuppressWarnings( "MagicNumber" )
    @API( status = STABLE, since = "0.0.5" )
    public static final String formatNodeIdAsMAC( final long nodeId )
    {
        if( nodeId != (nodeId & 0x0000FFFFFFFFFFFFL) )
        {
            throw new ValidationException( format( "Node id is invalid: %1$d", nodeId ) );
        }

        final var hexFormat = HexFormat.of();
        final var s = hexFormat.toHexDigits( nodeId, 12 );
        final var bytes = hexFormat.parseHex( s );
        final var retValue = HexFormat.ofDelimiter( "-" ).withUpperCase().formatHex( bytes );

        //---* Done *----------------------------------------------------------
        return retValue.toString();
    }   //  formatNodeIdAsMAC()

    /**
     *  Returns the MAC address for the current computer. In case the machine
     *  will have more than one network card (NIC), the returned MAC is
     *  selected in the same as for
     *  {@link #getNodeId()}.
     *
     *  @return The MAC address.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String getMACAddress() { return formatNodeIdAsMAC( getNodeId() ); }

    /**
     *  Returns a
     *  {@link Collection}
     *  of all the network interfaces on this machine. Instead of throwing an
     *  exception, the method will return an empty collection in case the
     *  machine do not have network configured. Otherwise, the collection
     *  contains at least one element, possibly representing a loopback
     *  interface that only supports communication between entities on this
     *  machine.
     *
     *  @return The
     *      {@link NetworkInterface}s found on this machine.
     *
     *  @see java.net.NetworkInterface#getNetworkInterfaces()
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Collection<NetworkInterface> getNetworkInterfaces()
    {
        Collection<NetworkInterface> retValue = List.of();
        try
        {
            retValue = List.copyOf( list( java.net.NetworkInterface.getNetworkInterfaces() ) );
        }
        catch( @SuppressWarnings( "unused" ) final SocketException e )
        {
            /*
             * There are no NICs on this machine, so we return the empty list.
             */
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getNetworkInterfaces()

    /**
     *  Returns the unique id of this node.
     *
     *  @return The node id.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final long getNodeId()
    {
        if( isNull( m_Node ) )
        {
            final var interfaces = getNetworkInterfaces();
            var randomGenerationRequired = interfaces.isEmpty();
            if( !randomGenerationRequired )
            {
                var count = interfaces.size();
                ScanLoop: for( final var nic : interfaces )
                {
                    try
                    {
                        if( (--count > 0) && (nic.isLoopback() || nic.isVirtual()) )
                        {
                            /*
                             * We prefer a real hardware NIC if we can get one.
                             */
                            continue ScanLoop;
                        }

                        final var hardwareAddress = nic.getHardwareAddress();
                        if( nonNull( hardwareAddress ) && (hardwareAddress.length > 0) )
                        {
                            final var i = new BigInteger( hardwareAddress );
                            //noinspection MagicNumber
                            m_Node = Long.valueOf( i.longValue() & 0x0000FFFFFFFFFFFFL );
                            break ScanLoop;
                        }
                    }
                    catch( @SuppressWarnings( "unused" ) final SocketException e )
                    {
                        /*
                         *  If a SocketException is caught here, this indicates
                         *  that we have problems to determine the NICs. In
                         *  this case, we have to create a random node id.
                         */
                        break ScanLoop;
                    }
                }   //  ScanLoop:

                /*
                 * If we still do not have a valid node id, it seems that none
                 * of the NICs have a hardware address; may be that they are
                 * NICs for a virtual machine or virtual NICs of some kind.
                 * But is it possible to have a NIC without a MAC?
                 */
                randomGenerationRequired = isNull( m_Node );
            }

            if( randomGenerationRequired )
            {
                //---* We have to create a random location *-------------------
                m_Node = Long.valueOf( createPseudoNodeId() );
            }
        }

        final var retValue = m_Node.longValue();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getNodeId()

    /**
     *  Returns the current operating system.
     *
     *  @return The operating system.
     */
    @API( status = STABLE, since = "0.0.6" )
    public static final OperatingSystem getOperatingSystem() { return m_OperatingSystem.get(); }

    /**
     *  Returns the PID, the process id, for the VM this (the current) program
     *  runs in.
     *
     *  @return The PID.
     *  @throws UnsupportedOperationException   The implementation of
     *      {@link ProcessHandle}
     *      that is currently in use does not support the operation
     *      {@link ProcessHandle#pid()}.
     *
     *  @see ProcessHandle#pid()
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final long getPID() throws UnsupportedOperationException
    {
        final var retValue = ProcessHandle.current().pid();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getPID()

    /**
     *  Returns a pseudo node id; this is useful in cases a machine does not
     *  have a network card (NIC) so that a MAC address could not be obtained,
     *  or if the real node id should not be used.
     *
     *  @return The node id.
     *
     *  @deprecated Use
     *      {@link #createPseudoNodeId()}
     *      instead.
     */
    @API( status = DEPRECATED )
    @Deprecated( forRemoval = true )
    public static long getPseudoNodeId() { return createPseudoNodeId(); }

    /**
     *  Returns the system random number generator.
     *
     *  @return The random number generator.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Random getRandom() { return m_Random.get(); }

    /**
     *  Returns the alias map; if not yet created, the alias map will be
     *  created by a call to
     *  {@link #createZoneIdAliasMap()}
     *  and the result to that call will be cached for future calls.
     *
     *  @return The alias map.
     *
     *  @see #createZoneIdAliasMap()
     *
     *  @since 0.0.6
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Map<String,String> getZoneIdAliasMap() { return m_ZoneIdAliasMap.get(); }

    /**
     *  Checks whether the current system has a network interface installed.
     *
     *  @return {@code true} if the current system has a network interface
     *      installed, {@code false} otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean hasNetworkInterface()
    {
        final var retValue = !getNetworkInterfaces().isEmpty();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hasNetworkInterface()

    /**
     *  Replaces the variables of the form <code>${<i>&lt;name&gt;</i>}</code>
     *  in the given String with values from the
     *  {@linkplain System#getProperties() system properties},
     *  the
     *  {@linkplain System#getenv() system environment}
     *  and the given maps. The method will try the maps in the given
     *  sequence.<br>
     *  <br>In addition, five more variables are recognised:
     *  <dl>
     *  <dt><b><code>tq.ip</code></b></dt>
     *  <dd>The first IP address for the machine that executes this Java
     *  virtual machine.</dd>
     *
     *  <dt><b><code>tq.mac</code></b></dt>
     *  <dd>The MAC address of the first NIC in this machine.</dd>
     *
     *  <dt><b><code>tq.nodeId</code></b></dt>
     *  <dd>The node id from the first NIC in this machine.</dd>
     *
     *  <dt><b><code>tq.now</code></b></dt>
     *  <dd>The current date and time as returned by
     *  {@link Instant#now}.</dd>
     *
     *  <dt><b><code>tq.pid</code></b></dt>
     *  <dd>The process id of this Java virtual machine.</dd>
     *  </dl>
     *  <br>If no replacement value could be found, the variable will not be
     *  replaced at all; no exception will be thrown.<br>
     *  <br>If a value from one of the maps contains a variable itself, this
     *  will not be replaced.<br>
     *  <br>The variables names are case-sensitive.
     *
     *  @param  text    The text with the variables; can be {@code null}.
     *  @param  additionalSources The maps with additional replacement values.
     *  @return The new text, or {@code null} if the provided value for
     *      {@code text} was already {@code null}.
     *
     *  @see Template#VARIABLE_PATTERN
     *  @see Template#replaceVariable(CharSequence, Map...)
     *
     *  @deprecated Use
     *      {@link Template#replaceVariableFromSystemData(CharSequence, Map[])}
     *      instead.
     */
    @SafeVarargs
    @Deprecated( since = "0.1.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.0.5" )
    public static final String replaceSystemVariable( final CharSequence text, final Map<String,? extends Object>... additionalSources )
    {
        return Template.replaceVariableFromSystemData( text, additionalSources );
    }   //  replaceSystemVariable()

    /**
     *  An implementation of
     *  {@link Thread#sleep}
     *  that does not throw an exception in case it will be interrupted.
     *
     *  @param  millis  The time to sleep in milliseconds.
     *  @return  {@code true} if the sleep was interrupted, {@code false} if it
     *      terminated as planned.
     */
    @SuppressWarnings( "BusyWait" )
    @API( status = STABLE, since = "0.0.7" )
    public static final boolean repose( final long millis )
    {
        var retValue = true;
        final var endTimeMillis  = currentTimeMillis() + millis;
        SleepLoop: while( currentTimeMillis() < endTimeMillis )
        {
            try
            {
                Thread.sleep( max( 0, endTimeMillis - currentTimeMillis() ) );
                retValue = false;
            }
            catch( @SuppressWarnings( "unused" ) final InterruptedException e )
            {
               if( currentThread().isInterrupted() ) break SleepLoop;
            }
        }   //  SleepLoop:

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  repose()

    /**
     *  An implementation of
     *  {@link #repose(long)}
     *  that takes an instance of
     *  {@link Duration}
     *  to determine the time to sleep.
     *
     *  @param  duration    The time to sleep.
     *  @return  {@code true} if the sleep was interrupted, {@code false} if it
     *      terminated as planned.
     */
    @API( status = STABLE, since = "0.0.7" )
    public static final boolean repose( final Duration duration )
    {
        final var milliseconds = requireNonNullArgument( duration, "duration" ).toMillis();
        final var retValue = repose( milliseconds );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  repose()

    /**
     *  An implementation of
     *  {@link #sleepUntil(Instant)}
     *  that does not throw an exception when interrupted.
     *
     *  @param  until   The end time for the sleep period.
     *  @return  {@code true} if the sleep was interrupted, {@code false} if it
     *      terminated as planned.
     */
    @API( status = STABLE, since = "0.0.7" )
    public static final boolean reposeUntil( final Instant until )
    {
        var retValue = true;
        SleepLoop: while( Instant.now().isBefore( requireNonNullArgument( until, "until" ) ) )
        {
            try
            {
                sleep( Duration.between( Instant.now(), until ) );
                retValue = false;
            }
            catch( @SuppressWarnings( "unused" ) final InterruptedException e )
            {
                if( currentThread().isInterrupted() ) break SleepLoop;
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  reposeUntil()
    /**
     *  Retrieves an instance of
     *  {@link Locale}
     *  for the given locale name. This method will lookup the requested locale
     *  first in the locales returned by
     *  {@link Locale#getAvailableLocales()}
     *  before it will create a new instance of {@code Locale} using
     *  {@link java.util.Locale.Builder Locale.Builder}.<br>
     *  <br>The method will return
     *  {@link Optional#empty()}
     *  when the format of the given {@code localeName} is invalid.<br>
     *  <br>If none of the already existing instances of {@code Locale} matches
     *  the given name, the language part, the country part, the variant part
     *  and the script part (if provided) are used to create a new instance. If
     *  extensions are given, they will be ignored.<br>
     *  <br>If the given locale name is empty or contains only whitespace, the
     *  method will return
     *  {@link Locale#ROOT}.
     *
     *  @param  localeName  The name of the locale.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the instance of {@code Locale} for the given name.
     */
    @SuppressWarnings( "AssignmentToNull" )
    @API( status = STABLE, since = "0.0.6" )
    public static final Optional<Locale> retrieveLocale( final CharSequence localeName )
    {
        final var name = requireNonNullArgument( localeName, "localeName" ).toString();
        final var locale = name.isBlank()
            ? ROOT
            : stream( Locale.getAvailableLocales() )
                  .filter( l -> l.toString().equals( name ) )
                  .findFirst()
                  .orElseGet( () ->
                  {
                      Locale result = null;
                      final var parts = splitString( name, '_' );
                      try
                      {
                          final var builder = new Locale.Builder();
                          ComposeSwitch: result = switch( parts.length )
                          {
                              case 1 -> builder.setLanguage( parts[0] ).build();
                              case 2 -> builder.setLanguage( parts[0] ).setRegion( parts[1] ).build();
                              case 3 -> {
                                  builder.setLanguage( parts[0] ).setRegion( parts[1] );
                                  if( parts[2].startsWith( "#" ) )
                                  {
                                      if( parts[2].length() == 4 )
                                          builder.setScript( parts[2].substring( 1 ) );
                                  }
                                  else
                                  {
                                      builder.setVariant( parts[2] );
                                  }
                                  yield builder.build();
                              }
                              case 4 -> {
                                  builder.setLanguage( parts[0] ).setRegion( parts[1] );
                                  if( parts[2].startsWith( "#" ) )
                                  {
                                      builder.setScript( parts[2].substring( 1 ) );
                                  }
                                  else
                                  {
                                      builder.setVariant( parts[2] );
                                      if( parts[3].startsWith( "#" ) )
                                      {
                                          if( parts[3].length() == 4 )
                                              builder.setScript( parts[3].substring( 1 ) );
                                      }
                                  }
                                  yield builder.build();
                              }
                              case 5 -> builder.setLanguage( parts[0] ).setRegion( parts[1] ).setVariant( parts[2] ).setScript( parts[3].substring( 1 ) ).build();
                              default -> null; // Invalid input ...
                          };    //  ComposeSwitch:
                      }
                      catch( @SuppressWarnings( "unused" ) final IllformedLocaleException e )
                      {
                          result = null;
                      }
                      return result;
                  } );

        final var retValue = Optional.ofNullable( locale );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveLocale()

    /**
     *  An implementation of
     *  {@link Thread#sleep}
     *  that takes an instance of
     *  {@link Duration}
     *  to determine the time to sleep.
     *
     *  @param  duration    The time to sleep.
     *  @throws InterruptedException    Another thread has interrupted the
     *      current thread (that one executing {@code sleep()}. The
     *      <i>interrupted status</i> of the current thread is cleared when
     *      this exception is thrown.
     */
    @API( status = STABLE, since = "0.0.7" )
    public static final void sleep( final Duration duration ) throws InterruptedException
    {
        final var milliseconds = requireNonNullArgument( duration, "duration" ).toMillis();
        Thread.sleep( milliseconds );
    }   //  sleep()

    /**
     *  Causes the currently executing thread to sleep (temporarily cease
     *  execution) until the specified time, subject to the precision and
     *  accuracy of system timers and schedulers. The thread does not lose
     *  ownership of any monitors.
     *
     *  @param  until   The end time for the sleep.
     *  @throws InterruptedException    Another thread has interrupted the
     *      current thread (that one executing {@code sleep()}. The
     *      <i>interrupted status</i> of the current thread is cleared when
     *      this exception is thrown.
     *
     *  @see Thread#sleep(long)
     *
     *  @since 0.0.7
     */
    @API( status = STABLE, since = "0.0.7" )
    public static final void sleepUntil( final Instant until ) throws InterruptedException
    {
        final var milliseconds = max( 0, requireNonNullArgument( until, "until" ).toEpochMilli() - currentTimeMillis() );
        Thread.sleep( milliseconds );
    }   //  sleepUntil()

    /**
     *  <p>{@summary Returns the
     *  {@linkplain System#getProperties() system properties}}
     *  as a
     *  {@link Map Map&lt;String,String&gt;}.</p>
     *  <p>{@link System#getProperties()}
     *  returns an instance of
     *  {@link java.util.Properties}, and that is implementing
     *  {@code Map<String,Object>}. Although it is unlikely that a value is
     *  <i>not</i> a String (if not impossible &hellip;), this method allows
     *  to enforce it.</p>
     *
     *  @note   A mere cast to {@code Map<String,String>} does not work
     *      &hellip;
     *
     *  @return A unmodifiable map with the system properties as Strings.
     */
    public static final Map<String,String> systemPropertiesAsStringMap()
    {
        final Map<String,String> map = new HashMap<>();
        final var properties = getProperties();
        for( final var key : properties.stringPropertyNames() )
        {
            map.put( key, properties.getProperty( key ) );
        }
        final var retValue = Map.copyOf( map );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  systemPropertiesAsStringMap()

    /**
     *  <p>{@summary Translates a given MAC address into a numerical node
     *  id.}</p>
     *  <p>A MAC address is a string consisting of 12 hex digits, grouped by
     *  two, each group separated by a hyphen:
     *  <code><i>xx</i>-<i>xx</i>-<i>xx</i>-<i>xx</i>-<i>xx</i>-<i>xx</i></code></p>
     *
     *  @param  macAddress  The MAC address to translate.
     *  @return The node id.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final long translateMACToNodeId( final String macAddress )
    {
        final var bytes = HexFormat.ofDelimiter( "-" ).withUpperCase().parseHex( requireNotEmptyArgument( macAddress, "macAddress" ) );
        final var MSG_InvalidMAC = "MAC address is invalid: %1$s";
        if( bytes.length != 6 )
        {
            throw new ValidationException( format( MSG_InvalidMAC, macAddress ) );
        }
        final byte [] bytes2 = new byte [7];
        arraycopy( bytes, 0, bytes2, 1, 6 );
        final var retValue = new BigInteger( bytes2 ).longValue();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translateMACToNodeId()
}
//  class SystemUtils

/*
 *  End of File
 */