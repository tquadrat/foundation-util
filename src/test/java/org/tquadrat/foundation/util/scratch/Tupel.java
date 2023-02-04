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

package org.tquadrat.foundation.util.scratch;

import static java.lang.Integer.signum;
import static java.util.Comparator.comparing;
import static org.apiguardian.api.API.Status.STABLE;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary A tupel.} The first entry, usually referred to as
 *  the &quot;key&quot; identifies the object, the second one is the
 *  &quot;value&quot;.</p>
 *
 *  @param  <K> The type of the key.
 *  @param  <V> The type of the value.
 *
 *  @param  key The key of the tupel.
 *  @param  value   The value of the tupel.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Tupel.java 1044 2023-02-04 09:58:19Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "NewClassNamingConvention" )
@ClassVersion( sourceVersion = "$Id: Tupel.java 1044 2023-02-04 09:58:19Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public record Tupel<K extends Comparable<K>,V>( K key, V value ) implements Comparable<Tupel<K,V>>
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final int compareTo( final Tupel<K,V> o )
    {
        final var retValue = signum( comparing( Tupel<K,V>::key ).compare( this, o ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  compareTo()
}
//  record Tupel

/*
 *  End of File
 */