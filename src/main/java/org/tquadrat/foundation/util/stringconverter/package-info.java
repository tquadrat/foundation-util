/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 * http://www.gnu.org/licenses/lgpl.html
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/**
 *  This package provides classes that allow to convert certain object
 *  instances into String and vice versa. These are inspired by the abstract
 *  class {@code javafx.util.StringConverter<T>} from JavaFX, and usually, we
 *  would use that class as the base for our implementation. But as JavaFX is
 *  no longer part of the Java distribution, we have to provide our own
 *  implementation.<br>
 *  <br>There is no implementation of
 *  {@link org.tquadrat.foundation.lang.StringConverter}
 *  for {@code java.util.Date} as the class does not provide a proper parser
 *  for dates, and because the method {@code java.util.Date#toString()} does
 *  not emit the milliseconds so that the condition
 *  <pre><code>  StringConverter&lt;java.util.Date&gt; c = &hellip;
 *  java.util.Date v = &hellip;
 *  true == ( v.equals( c.fromString( c.toString( v ) );</code></pre>
 *  cannot be easily satisfied for all possible values of {@code v} (and not to
 *  mention that the class is seen as obsolete).<br>
 *  <br>There are implementations of
 *  {@link org.tquadrat.foundation.lang.StringConverter}
 *  for a few {@code enum} types, like
 *  {@link java.time.DayOfWeek}
 *  and
 *  {@link java.time.Month},
 *  additional ones can be implemented easily by using
 *  {@link org.tquadrat.foundation.util.stringconverter.EnumStringConverter}
 *  as base class (like it was done for the already existing ones), or by using
 *  that class directly, like here:<pre><code>  &hellip;
 *  StringConverter&lt;java.nio.file.attribute.PosixFilePermission&gt; pfpStringConverter = new EnumStringConverter&lt;&gt;( java.nio.file.attribute.PosixFilePermission.class );
 *  &hellip;</code></pre>
 *  Alternatively, the method
 *  {@link org.tquadrat.foundation.lang.StringConverter#forEnum(Class) StringConverter.forEnum()}
 *  can be used; basically, it does the same as the code snippet above.
 */

package org.tquadrat.foundation.util.stringconverter;

/*
 *  End of File
 */