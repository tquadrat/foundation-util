/*
 * ============================================================================
 * Copyright © 2002-2022 by Thomas Thrien.
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
 *  <p>{@summary This package provides classes that allow to convert certain
 *  object instances into String and vice versa.} These are inspired by the
 *  abstract class {@code javafx.util.StringConverter<T>} from JavaFX, and
 *  originally, I intended to use that class as the base for my implementation.
 *  But as JavaFX is no longer part of the Java distribution, I have to provide
 *  my own implementation:
 *  {@link org.tquadrat.foundation.lang.StringConverter}.</p>
 *  <p>An additional implementations can be retrieved by
 *  {@link org.tquadrat.foundation.lang.StringConverter#forClass(java.lang.Class)}
 *  when it was published as a
 *  {@linkplain java.util.ServiceLoader service}.
 *  In that case it must be taken care that the <i>subject class</i> of the new
 *  implementation does not collide with that of an already existing
 *  implementation. But it is still possible to provide an alternative
 *  conversion – in that case, the implementation needs to be retrieved
 *  explicitly.</p>
 *  <p>There is no implementation of a {@code StringConverter} for
 *  {@code java.util.Date} that works with human readable formats for the a
 *  date/time value, because the class does not provide a proper parser
 *  for dates, and because the method {@code java.util.Date#toString()} does
 *  not emit the milliseconds so that the condition</p>
 *  <pre><code>  StringConverter&lt;java.util.Date&gt; c = &hellip;
 *  java.util.Date v = &hellip;
 *  true == ( v.equals( c.fromString( c.toString( v ) );</code></pre>
 *  <p>cannot be easily satisfied for all possible values of {@code v} (and not
 *  to mention that the class {@code java.util.Date} is seen as obsolete).</p>
 *  <p>But I provide the class
 *  {@link org.tquadrat.foundation.util.stringconverter.DateLongStringConverter}
 *  that converts an instance of {@code java.util.Date} to a String holding the
 *  number of milliseconds since the begin of the epoch and vice versa. But
 *  this cannot be retrieved through
 *  {@code StringConverter.forClass( java.util.Date.class )}.</p>
 *  <p>This package hold implementations of
 *  {@link org.tquadrat.foundation.lang.StringConverter}
 *  for a few {@code enum} types, like
 *  {@link java.time.DayOfWeek}
 *  and
 *  {@link java.time.Month},
 *  additional String converters can be implemented easily by using
 *  {@link org.tquadrat.foundation.util.stringconverter.EnumStringConverter}
 *  as base class (like it was done for the already existing ones), or by using
 *  that class directly, like here:</p>
 *  <pre><code>  &hellip;
 *  StringConverter&lt;java.nio.file.attribute.PosixFilePermission&gt; pfpStringConverter = new EnumStringConverter&lt;&gt;( java.nio.file.attribute.PosixFilePermission.class );
 *  &hellip;</code></pre>
 *  <p>Alternatively, the method
 *  {@link org.tquadrat.foundation.lang.StringConverter#forEnum(Class) StringConverter.forEnum()}
 *  can be used; basically, it does the same as the code snippet above.</p>
 */
@API( status = STABLE, since = "0.0.1" )
package org.tquadrat.foundation.util.stringconverter;

import static org.apiguardian.api.API.Status.STABLE;

import org.apiguardian.api.API;

/*
 *  End of File
 */