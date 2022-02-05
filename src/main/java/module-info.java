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

import org.tquadrat.foundation.util.stringconverter.BigDecimalStringConverter;
import org.tquadrat.foundation.util.stringconverter.BigIntegerStringConverter;
import org.tquadrat.foundation.util.stringconverter.BooleanStringConverter;
import org.tquadrat.foundation.util.stringconverter.ByteArrayStringConverter;
import org.tquadrat.foundation.util.stringconverter.ByteStringConverter;
import org.tquadrat.foundation.util.stringconverter.CharSequenceStringConverter;
import org.tquadrat.foundation.util.stringconverter.CharacterStringConverter;
import org.tquadrat.foundation.util.stringconverter.CharsetStringConverter;
import org.tquadrat.foundation.util.stringconverter.ClassStringConverter;
import org.tquadrat.foundation.util.stringconverter.CurrencyStringConverter;
import org.tquadrat.foundation.util.stringconverter.DayOfWeekStringConverter;
import org.tquadrat.foundation.util.stringconverter.DoubleStringConverter;
import org.tquadrat.foundation.util.stringconverter.DurationStringConverter;
import org.tquadrat.foundation.util.stringconverter.FileStringConverter;
import org.tquadrat.foundation.util.stringconverter.FloatStringConverter;
import org.tquadrat.foundation.util.stringconverter.InetAddressStringConverter;
import org.tquadrat.foundation.util.stringconverter.InstantStringConverter;
import org.tquadrat.foundation.util.stringconverter.IntegerStringConverter;
import org.tquadrat.foundation.util.stringconverter.LocalDateStringConverter;
import org.tquadrat.foundation.util.stringconverter.LocalDateTimeStringConverter;
import org.tquadrat.foundation.util.stringconverter.LocalTimeStringConverter;
import org.tquadrat.foundation.util.stringconverter.LocaleStringConverter;
import org.tquadrat.foundation.util.stringconverter.LongStringConverter;
import org.tquadrat.foundation.util.stringconverter.MonthStringConverter;
import org.tquadrat.foundation.util.stringconverter.PathStringConverter;
import org.tquadrat.foundation.util.stringconverter.PatternStringConverter;
import org.tquadrat.foundation.util.stringconverter.PeriodStringConverter;
import org.tquadrat.foundation.util.stringconverter.ShortStringConverter;
import org.tquadrat.foundation.util.stringconverter.StringStringConverter;
import org.tquadrat.foundation.util.stringconverter.TimeUnitStringConverter;
import org.tquadrat.foundation.util.stringconverter.TimeZoneStringConverter;
import org.tquadrat.foundation.util.stringconverter.URIStringConverter;
import org.tquadrat.foundation.util.stringconverter.URLStringConverter;
import org.tquadrat.foundation.util.stringconverter.UUIDStringConverter;
import org.tquadrat.foundation.util.stringconverter.YearMonthStringConverter;
import org.tquadrat.foundation.util.stringconverter.YearStringConverter;
import org.tquadrat.foundation.util.stringconverter.ZoneIdStringConverter;
import org.tquadrat.foundation.util.stringconverter.ZonedDateTimeStringConverter;

/**
 *  The module for the utility and helper classes of the Foundation Library.
 *
 *  @provides   org.tquadrat.foundation.lang.StringConverter    Implementations
 *      of String converters.
 *
 *  @version $Id: module-info.java 1007 2022-02-05 01:03:43Z tquadrat $
 *
 *  @todo task.list
 */
module org.tquadrat.foundation.util
{
    requires java.base;
    requires transitive org.tquadrat.foundation.base;
    requires transitive java.compiler;

    //---* Common Use *--------------------------------------------------------
    exports org.tquadrat.foundation.util;
    exports org.tquadrat.foundation.util.stringconverter;

    provides org.tquadrat.foundation.lang.StringConverter with
        BigDecimalStringConverter,
        BigIntegerStringConverter,
        BooleanStringConverter,
        ByteArrayStringConverter,
        ByteStringConverter,
        CharacterStringConverter,
        CharSequenceStringConverter,
        CharsetStringConverter,
        ClassStringConverter,
        CurrencyStringConverter,
        DayOfWeekStringConverter,
        DoubleStringConverter,
        DurationStringConverter,
        FileStringConverter,
        FloatStringConverter,
        InetAddressStringConverter,
        InstantStringConverter,
        IntegerStringConverter,
        LocalDateStringConverter,
        LocalDateTimeStringConverter,
        LocaleStringConverter,
        LocalTimeStringConverter,
        LongStringConverter,
        MonthStringConverter,
        PathStringConverter,
        PatternStringConverter,
        PeriodStringConverter,
        ShortStringConverter,
        StringStringConverter,
        TimeUnitStringConverter,
        TimeZoneStringConverter,
        URIStringConverter,
        URLStringConverter,
        UUIDStringConverter,
        YearMonthStringConverter,
        YearStringConverter,
        ZonedDateTimeStringConverter,
        ZoneIdStringConverter;
}

/*
 *  End of File
 */