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

package org.tquadrat.foundation.util.arrayutils;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.ArrayUtils.revert;
import static org.tquadrat.foundation.util.StringUtils.format;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.ArrayUtils;

/**
 *  This class tests the methods in
 *  {@link ArrayUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestRevert.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "StringOperationCanBeSimplified" )
@ClassVersion( sourceVersion = "$Id: TestRevert.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.arrayutils.TestRevert" )
public class TestRevert extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for
     *  {@link ArrayUtils#revert(Object[])}
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testRevert() throws Exception
    {
        skipThreadTest();

        String [] values;
        String [] expected;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        values = null;
        try
        {
            revert( values );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        values = new String [0];
        expected = new String [0];
        assertNotSame( expected, values );
        revert( values );
        assertArrayEquals( expected, values );

        values = new String [] {"one"};
        expected = values.clone();
        assertNotSame( expected, values );
        revert( values );
        assertArrayEquals( expected, values );

        values = new String [] {"one", "two"};
        expected = new String [] {"two", "one"};
        assertNotSame( expected, values );
        revert( values );
        assertArrayEquals( expected, values );

        values = new String [] {"one", "two", "three"};
        expected = new String [] {"three", "two", "one"};
        assertNotSame( expected, values );
        revert( values );
        assertArrayEquals( expected, values );

        values = new String [] {"one", "two", "three", "four"};
        expected = new String [] {"four", "three", "two", "one"};
        assertNotSame( expected, values );
        revert( values );
        assertArrayEquals( expected, values );

        values = new String [] {"one", "two", "three", "four", "five"};
        expected = new String [] {"five", "four", "three", "two", "one"};
        assertNotSame( expected, values );
        revert( values );
        assertArrayEquals( expected, values );
    }   //  testRevert()
}
//  class TestRevert

/*
 *  End of File
 */