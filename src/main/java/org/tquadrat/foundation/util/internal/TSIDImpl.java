/*
 * ============================================================================
 *  Copyright © 2002-2023 by Thomas Thrien.
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

package org.tquadrat.foundation.util.internal;

import static java.lang.Integer.signum;
import static java.lang.Long.compare;
import static java.lang.String.format;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.util.Base32.getEncoder;
import static org.tquadrat.foundation.util.StringUtils.repeat;
import static org.tquadrat.foundation.util.SystemUtils.getRandom;

import java.time.Instant;
import java.util.Objects;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.util.Base32.Encoder;
import org.tquadrat.foundation.util.TSID;

/**
 *  The implementation for the interface
 *  {@link TSID}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TSIDImpl.java 1037 2022-12-15 00:35:17Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: TSIDImpl.java 1037 2022-12-15 00:35:17Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class TSIDImpl implements TSID
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The internal representation of a TSID is a {@code long} value.
     */
    private final long m_Id;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The beginning of the epoch for these ids.
     */
    public static final Instant BEGIN_OF_EPOCH_INSTANT;

    /**
     *  The node number for the TSIDs.
     */
    public static final long TSID_Node;

    /**
     *  The shift to convert the current time into the time-base used by this
     *  class.
     */
    public static final long SHIFT;

    /**
     *  The encoder for
     *  {@link #toString()}.
     */
    private static final Encoder m_Encoder;

    static
    {
        BEGIN_OF_EPOCH_INSTANT = Instant.parse( BEGIN_OF_EPOCH );
        SHIFT = BEGIN_OF_EPOCH_INSTANT.toEpochMilli();

        final var nodeId = Integer.getInteger( PROPERTY_TSID_NODE, getRandom().nextInt( MAX_NODES ) );
        if( nodeId < MAX_NODES )
        {
            TSID_Node = (long) nodeId;
        }
        else
        {
            //noinspection LocalVariableNamingConvention
            final var e = new ValidationException( "Node id '%d' is too large, value must be less than %d".formatted( nodeId, MAX_NODES ) );
            throw new ExceptionInInitializerError( e );
        }

        m_Encoder = getEncoder();
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code TSIDImpl}.
     *
     *  @param  id  The internal representation of a TSID is a {@code long}
     *      value.
     */
    public TSIDImpl( final long id )
    {
        m_Id = id;
    }   //  TSIDImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final long asLong() { return m_Id; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public int compareTo( final TSID other )
    {
        final var retValue = signum( compare( m_Id, other.asLong() ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  compareTo()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean equals( final Object o )
    {
        var retValue = this == o;
        if( !retValue && o instanceof final TSIDImpl other )
        {
            retValue = m_Id == other.m_Id;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  equals()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return Objects.hash( m_Id ); }

    /**
     *   {@inheritDoc}
     */
    @Override
    public final String toString()
    {
        final var base32 = m_Encoder.encodeToString( m_Id );
        final var retValue = format( "X%s%s", repeat( "0", TSID_Size - 1 - base32.length() ), base32 );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class TSIDImpl

/*
 *  End of File
 */