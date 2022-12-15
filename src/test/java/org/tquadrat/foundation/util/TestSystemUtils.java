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

package org.tquadrat.foundation.util;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.lang.reflect.Field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Some tests for the class
 *  {@link org.tquadrat.foundation.util.SystemUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestSystemUtils.java 1037 2022-12-15 00:35:17Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestSystemUtils.java 1037 2022-12-15 00:35:17Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.SystemUtils" )
public class TestSystemUtils extends TestBaseClass
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The field for the node id.
     */
    private static final Field m_NodeField;

    /**
     *  The field for the node id bits.
     */
    private static final Field m_NodeIdBitsField;

    /**
     *  The field for the node id sign.
     */
    private static final Field m_NodeIdSignField;

    static
    {
        final var targetClass = SystemUtils.class;
        String fieldName = null;

        try
        {
            fieldName = "m_Node";
            m_NodeField = targetClass.getDeclaredField( fieldName );
            m_NodeField.setAccessible( true );

            fieldName = "m_NodeIdBits";
            m_NodeIdBitsField = targetClass.getDeclaredField( fieldName );
            m_NodeIdBitsField.setAccessible( true );

            fieldName = "m_NodeIdSign";
            m_NodeIdSignField = targetClass.getDeclaredField( fieldName );
            m_NodeIdSignField.setAccessible( true );
        }
        catch( final NoSuchFieldException e )
        {
            throw new ExceptionInInitializerError( new RuntimeException( format( "No field '%1$s' in class '%2$s'", fieldName, targetClass.getName() ), e ) );
        }
    }
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets a fixed value for the node id.
     *
     *  @param  nodeId  The node id.
     */
    public final void setNode( final long nodeId ) throws IllegalAccessException
    {
        final var bits = m_NodeIdBitsField.getLong( null );
        final var sign = m_NodeIdSignField.getLong( null );
        if( ((nodeId & bits ) | sign) != nodeId ) throw new IllegalArgumentException( format( "Value '%d' is not valid for a nodeId", nodeId ) );
        m_NodeField.set( null, Long.valueOf( nodeId ) );
    }   //  setNode()

    /**
     *  Validates whether the class is static.
     */
    @Test
    final void validateClass()
    {
        assertTrue( validateAsStaticClass( SystemUtils.class ) );
    }   //  validateClass()
}
//  class TestSystemUtils

/*
 *  End of File
 */