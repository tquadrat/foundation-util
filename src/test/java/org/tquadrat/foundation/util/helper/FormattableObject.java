/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

package org.tquadrat.foundation.util.helper;

import org.tquadrat.foundation.annotation.ClassVersion;

import java.time.Instant;
import java.util.Formattable;
import java.util.Formatter;

import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

/**
 *  An implementation of
 *  {@link Formattable}
 *  used for testing, where
 *  {@link #toString()}
 *  returns something else than
 *  {@link #formatTo(Formatter,int,int,int)}.<br>
 *  <br>Modifying this class may break several tests.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: FormattableObject.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 9
 */
@ClassVersion( sourceVersion = "$Id: FormattableObject.java 820 2020-12-29 20:34:22Z tquadrat $" )
public class FormattableObject implements Formattable
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code FormattableObject} objects.
     */
    public static final FormattableObject [] EMPTY_FormattableObject_ARRAY = new FormattableObject [0];

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The value.
     */
    private final Instant m_Value;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code FormattableObject} instance.
     *
     *  @param  value   The value.
     */
    public FormattableObject( final Instant value)
    {
        m_Value = requireNonNullArgument( value, "value" );
    }   //  FormattableObject()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final void formatTo( final Formatter formatter, final int flags, final int width, final int precision )
    {
        final var className = String.format( "Classname: %s", getClass().getName() );
        final var s = String.format( "%2$s: Now: %1$s (never too late!)", m_Value, className );
        formatter.format( s );
        formatter.format( s );
    }   //  formatTo()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return "FormattableObject []"; }
}
//  class FormattableObject

/*
 *  End of File
 */