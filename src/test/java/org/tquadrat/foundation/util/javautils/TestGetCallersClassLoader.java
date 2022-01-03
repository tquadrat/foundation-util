/*
 * ============================================================================
 * Copyright © 2002-2018 by Thomas Thrien.
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

package org.tquadrat.foundation.util.javautils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.JavaUtils;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.tquadrat.foundation.util.JavaUtils.getCallersClassLoader;

/**
 *  Test for the method
 *  {@link JavaUtils#getCallersClassLoader()}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestGetCallersClassLoader.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.javautils.TestGetCallersClassLoader" )
public class TestGetCallersClassLoader extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Calls
     *  {@link JavaUtils#getCallersClassLoader()}
     *  and returns the result.
     *
     *  @return The caller's class loader.
     */
    private static final ClassLoader callGetCallersClassLoader()
    {
        return getCallersClassLoader();
    }   //  callGetCallersClassLoader()

    /**
     *  Tests for the method
     *  {@link JavaUtils#getCallersClassLoader()}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testGetCallersClassLoader() throws Exception
    {
        skipThreadTest();

        //---* The class loader for this class *-------------------------------
        final var expected = getClass().getClassLoader();
        var actual = callGetCallersClassLoader();
        assertSame( expected, actual );

        //---* What if, when we call the method from a lambda? *---------------
        Supplier<ClassLoader> lambda = TestGetCallersClassLoader::callGetCallersClassLoader;
        actual = lambda.get();
        assertNotNull( actual );
        assertSame( expected, actual );

        lambda = JavaUtils::getCallersClassLoader;
        actual = lambda.get();
        assertNotNull( actual );
    }   //  testGetCallersClassLoader()
}
//  class TestGetCallersClassLoader

/*
 *  End of File
 */