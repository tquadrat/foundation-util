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

package org.tquadrat.foundation.util.internal;

import static org.apiguardian.api.API.Status.STABLE;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.RangeMap;

/**
 * An implementation of {@code RangeMap} that does not allow changes.
 *
 *  @param <T> The type of the mapped value.
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: FinalRangeMap.java 995 2022-01-23 01:09:35Z tquadrat $
 *  @UMLGraph.link
 *  @since 0.0.7
 */
@ClassVersion( sourceVersion = "$Id: FinalRangeMap.java 995 2022-01-23 01:09:35Z tquadrat $" )
@API( status = STABLE, since = "0.0.7" )
public final class FinalRangeMap<T> extends RangeMapImpl<T>
{
        /*------------------------*\
    ====** Static Initialisations **=======================================
        \*------------------------*/
    /**
     * The serial version UID for objects of this class: {@value}.
     *
     * @hidden
     */
    @Serial
    private static final long serialVersionUID = 1;

        /*--------------*\
    ====** Constructors **=================================================
        \*--------------*/
    /**
     * Creates a new {@code FinalRangeMap} instance from the given
     * instance of {@code RangeMap}.
     *
     * @param other The other
     *     {@link RangeMapImpl}.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public FinalRangeMap( final RangeMapImpl<? extends T> other )
    {
        super( other );
    }   //  FinalRangeMap()

        /*---------*\
    ====** Methods **======================================================
        \*---------*/

    /**
     * This method will always throw an
     * {@link UnsupportedOperationException}.
     *
     * @return {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public final FinalRangeMap<T> addRange( final double key, final T value ) {throw new UnsupportedOperationException( "addRange" );}

    /**
     * This method will always throw an
     * {@link UnsupportedOperationException}.
     */
    @Override
    public final void clear() {throw new UnsupportedOperationException( "clear" );}

    /**
     * This method will always throw an
     * {@link UnsupportedOperationException}.
     */
    @Override
    public final RangeMap<T> removeRange( final double key ) {throw new UnsupportedOperationException( "removeRange" );}

    /**
     * This method will always throw an
     * {@link UnsupportedOperationException}.
     *
     * @return {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public final FinalRangeMap<T> setDefault( final T defaultValue ) {throw new UnsupportedOperationException( "setDefault" );}
}
//  class FinalRangeMap

/*
 *  End of File
 */