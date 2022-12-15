/*
 * ============================================================================
 *  Copyright © 2002-2022 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.util;

import static org.apiguardian.api.API.Status.STABLE;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.internal.TSIDImpl;

/**
 *  <p>{@summary The definition for a 64-bit time-based and time-sorted unique
 *  id.}</p>
 *  <p>The idea behind this implementation of a unique key is to have a shorter
 *  version of a unique key as that provided by the class
 *  {@link java.util.UUID}
 *  with its 128-bit length.</p>
 *  <p>In addition, the text representation of a TSID can also be used for XML
 *  and HTML ids (an XML id may not begin with a digit).</p>
 *  <p>A TSID has a 42-bit timestamp – the milliseconds since
 *  {@value #BEGIN_OF_EPOCH}, a 12-bit counter (the number of ids that can be
 *  created within the same millisecond), and a 10-bit node identifier. This
 *  means that for a given node identifier no collision will happen before the
 *  year 2160.</p>
 *  <p>The node identifier can be set through a JVM command parameter named
 *  {@value #PROPERTY_TSID_NODE}, or it can be given as an argument to the
 *  factory method. If the property is not set, a random value will be
 *  determined at program start.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TSID.java 1037 2022-12-15 00:35:17Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 *
 *  @see UniqueIdUtils#newTSID()
 *  @see UniqueIdUtils#newTSID(int)
 */
@SuppressWarnings( "NewClassNamingConvention" )
@ClassVersion( sourceVersion = "$Id: TSID.java 1037 2022-12-15 00:35:17Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public sealed interface TSID extends Comparable<TSID>
    permits TSIDImpl
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The start of the epoch for the TSIDs: {@value}.
     */
    public static final String BEGIN_OF_EPOCH = "2022-01-01T00:00:00.000Z";

    /**
     *  The maximum number of ids that can be generated per millisecond:
     *  {@value}.
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    public static final int MAX_IDS_PER_MILLISECOND = 4096; // 12 bit

    /**
     *  The maximum number of distinguishable nodes for the TSID generation:
     *  {@value}.
     */
    public static final int MAX_NODES = 1024; // 10 bit

    /**
     *  The property name for the node value that is used for the generation of
     *  the ids: {@value}.
     */
    public static final String PROPERTY_TSID_NODE = "org.tquadrat.util.tsid.node";

    /**
     *  The length of a TSID String: {@value}.
     */
    public static final int TSID_Size = 14;

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the id as a {@code long} value.
     *
     *  @return The numeric value that represents this TSID instance.
     */
    public long asLong();

    /**
     *  {@inheritDoc}
     */
    @Override
    public int compareTo( final TSID other );

    /**
     *  {@inheritDoc}
     */
    @Override
    public boolean equals( final Object o );

    /**
     *  {@inheritDoc}
     */
    @Override
    public int hashCode();

    /**
     *  <p>{@summary Returns a String representation for the TSID.} Basically,
     *  this is the
     *  {@link Base32}
     *  format, using
     *  {@href https://www.crockford.com/base32.html Crockfords's schema},
     *  prefixed with the letter 'X'. The prefix allows to use the TSID also as
     *  an XML id.</p>
     */
    @Override
    public String toString();
}
//  interface TSID

/*
 *  End of File
 */