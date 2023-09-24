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

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Comparator;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  An implementation of
 *  {@link Comparator}
 *  that compares object instances based on their String representation.
 *
 *  @param  <T> The type of the objects to compare.
 *
 *  @version $Id: StringBasedComparator.java 1007 2022-02-05 01:03:43Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: StringBasedComparator.java 1007 2022-02-05 01:03:43Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public class StringBasedComparator<T> implements Comparator<T>
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The instance of
     *  {@link StringConverter}
     *  that is used to translate the object instances to Strings.
     */
    private final StringConverter<T> m_StringConverter;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code StringBasedComparator}.
     *
     *  @param  stringConverter The instance of
     *      {@link StringConverter}
     *      that is used to translate the object instances to Strings.
     */
    public StringBasedComparator( final StringConverter<T> stringConverter )
    {
        m_StringConverter = requireNonNullArgument( stringConverter, "stringConverter" );
    }   //  StringBaseComparator()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public int compare( final T o1, final T o2 )
    {
        return Integer.signum( Comparator.<String>naturalOrder().compare( m_StringConverter.toString( o1 ), m_StringConverter.toString( o2 ) ) );
    }   //  compare()
}
//  class StringBasedComparator

/*
 *  End of File
 */