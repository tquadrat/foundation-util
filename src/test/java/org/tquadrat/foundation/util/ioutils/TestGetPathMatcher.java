/*
 * ============================================================================
 *  Copyright © 2002-2026 by Thomas Thrien.
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

package org.tquadrat.foundation.util.ioutils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.util.IOUtils.getPathMatcher;
import static org.tquadrat.foundation.util.IOUtils.parseFilePatterns;

import java.nio.file.PathMatcher;
import java.util.Collection;
import java.util.regex.PatternSyntaxException;

import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.IOUtils;

/**
 *  <p>{@summary @TODO Comment for TestGetPathMatcher.}</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestGetPathMatcher.java 1228 2026-05-04 12:21:25Z tquadrat $
 *  @since 0.25.0
 */
@ClassVersion( sourceVersion = "$Id: TestGetPathMatcher.java 1228 2026-05-04 12:21:25Z tquadrat $" )
public class TestGetPathMatcher extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Some tests for
     *  {@link IOUtils#getPatchMatcher(String)},
     *  {@link org.tquadrat.foundation.util.IOUtils#parseFilePatterns(CharSequence...)}
     *  and
     *  {@link org.tquadrat.foundation.util.IOUtils#parseFilePatterns(Collection)}.
     *
     *  @throws Exception   Something went awfully wrong.
     */
    @Test
    final void testGetPathMatcher() throws Exception
    {
        skipThreadTest();

        assertThrows( NullArgumentException.class, () -> getPathMatcher( null ) );
        assertInstanceOf( PathMatcher.class, assertDoesNotThrow( () -> getPathMatcher( EMPTY_CHARSEQUENCE ) ) );
        assertInstanceOf( PathMatcher.class, assertDoesNotThrow( () -> getPathMatcher( " " ) ) );

        assertThrows( UnsupportedOperationException.class, () -> getPathMatcher( "ThisIsAnUnsupportedSyntax:*" ) );
        assertInstanceOf( PathMatcher.class, assertDoesNotThrow( () -> getPathMatcher( "*" ) ) );
        assertInstanceOf( PathMatcher.class, assertDoesNotThrow( () -> getPathMatcher( "." ) ) );
        assertInstanceOf( PathMatcher.class, assertDoesNotThrow( () -> getPathMatcher( "regex:." ) ) );

        assertThrows( PatternSyntaxException.class, () -> getPathMatcher( "regex:***" ) );

        assertThrows( NullArgumentException.class, () -> parseFilePatterns( (CharSequence[]) null ) );
        assertThrows( NullArgumentException.class, () -> parseFilePatterns( (Collection<? extends CharSequence>) null ) );

        Collection<PathMatcher> result;
        result = assertDoesNotThrow( () -> parseFilePatterns( "*", "regex:." ) );
        result.forEach( v -> assertInstanceOf( PathMatcher.class, v ) );

        Throwable e;
        e = assertThrows( IllegalArgumentException.class, () -> parseFilePatterns( null, "*" ) );
        assertNull( e.getCause() );
        e = assertThrows( IllegalArgumentException.class, () -> parseFilePatterns( "regex:***", "*" ) );
        assertInstanceOf( PatternSyntaxException.class, e.getCause() );
        e = assertThrows( IllegalArgumentException.class, () -> parseFilePatterns( "ThisIsAnUnsupportedSyntax:*", "*" ) );
        assertInstanceOf( UnsupportedOperationException.class, e.getCause() );
    }   //  testGetPathMatcher()
}
//  class TestGetPathMatcher

/*
 *  End of File
 */