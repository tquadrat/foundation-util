/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 *
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

package org.tquadrat.foundation.util.stringconverter;

import static org.apiguardian.api.API.Status.STABLE;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;
import org.tquadrat.foundation.lang.internal.DefaultEnumStringConverter;

/**
 *  An implementation of
 *  {@link StringConverter}
 *  for types that are derived from
 *  {@link Enum}.
 *  This class can be used as is, or as base class for the implementation of
 *  {@code StringConverter} for a specific {@code enum}. A sample for the
 *  latter are the {@code StringConverter} implementations for
 *  {@link java.time.DayOfWeek}
 *  ({@link DayOfWeekStringConverter})
 *  and
 *  {@link java.time.Month}
 *  ({@link MonthStringConverter}).
 *
 *  @param  <T> The concrete data type that is handled by this String converter
 *      implementation.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: EnumStringConverter.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.6
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: EnumStringConverter.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = STABLE, since = "0.0.6" )
public class EnumStringConverter<T extends Enum<T>> extends DefaultEnumStringConverter<T>
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code EnumValueHandler} instance.
     *
     *  @param  enumType    The data type for the property.
     */
    public EnumStringConverter( final Class<T> enumType ) { super( enumType ); }
}
//  class EnumStringConverter

/*
 *  End of File
 */