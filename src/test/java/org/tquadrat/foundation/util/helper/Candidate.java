/*
 * ============================================================================
 * Copyright © 2002-2018 by Thomas Thrien.
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

package org.tquadrat.foundation.util.helper;

import static java.lang.System.err;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;

/**
 *  A dummy class, just for some testing.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@SuppressWarnings( {"ClassWithTooManyMethods", "RedundantNoArgConstructor"} )
@ClassVersion( sourceVersion = "$Id: Candidate.java 820 2020-12-29 20:34:22Z tquadrat $" )
public class Candidate implements Serializable
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code Candidate} objects.
     */
    public static final Candidate [] EMPTY_Candidate_ARRAY = new Candidate [0];

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  Attribute 1.
     */
    private String m_Attribute1;

    /**
     *  Attribute 2.
     */
    private boolean m_Attribute2;

    /**
     *  Attribute 3.
     */
    private String m_Attribute3;

    /**
     *  Attribute 4.
     */
    @SuppressWarnings( "unused" )
    private String m_Attribute4;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     */
    @Serial
    private static final long serialVersionUID = 1L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code Candidate} object.
     */
    public Candidate()
    {
        //  Does nothing ...
    }   //  Candidate()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets a new value for
     *  {@link #m_Attribute4}.<br>
     *  <br>This is a hidden setter.
     *
     *  @param attribute4 The new value for attribute4.
     */
    @SuppressWarnings( "unused" )
    private final void attribute4Setter( final String attribute4 )
    {
        m_Attribute1 = attribute4;
    }   //  attribute4Setter()

    /**
     *  A dummy method.
     */
    @SuppressWarnings( "unused" )
    private void doNothing() { /* Does nothing. */ }

    /**
     *  A dummy method.
     */
    public static void doNothingStatic() { /* Does nothing. */ }

    /**
     *  This is not equals.
     *
     *  @return {@code false}.
     */
    @SuppressWarnings( "static-method" )
    public final boolean equals() { return false; }

    /**
     *  A dummy method.
     *
     *  @return The empty String.
     */
    @SuppressWarnings( "static-method" )
    public final String get() { return EMPTY_STRING; }

    /**
     *  A dummy method.
     *
     *  @return The empty String.
     */
    @SuppressWarnings( "static-method" )
    public final String getter() { return EMPTY_STRING; }

    /**
     *  A dummy method.
     */
    public final void getNothing() { /* Does nothing */ }

    /**
     *  Returns the current value of
     *  {@link #m_Attribute1}.
     *
     *  @return The attribute1.
     */
    public final String getAttribute1() { return m_Attribute1; }

    /**
     *  Returns the current value of
     *  {@link #m_Attribute3}.
     *
     *  @return The attribute3.
     */
    @SuppressWarnings( "unused" )
    private final String getAttribute3() { return m_Attribute3; }

    /**
     *  Returns the current value of
     *  {@link #m_Attribute4}.
     *
     *  @return The attribute4.
     */
    public final String getAttribute4() { return m_Attribute4; }

    /**
     *  Not the hash code method.
     *
     *  @param  arg An argument.
     *  @return -1.
     */
    @SuppressWarnings( {"static-method", "unused"} )
    public final int hashCode( final int arg ) { return -1; }

    /**
     *  A dummy method.
     *
     *  @return {@code true}.
     */
    @SuppressWarnings( "static-method" )
    public final boolean is() { return true; }

    /**
     *  A dummy method.
     *
     *  @return {@code false}.
     */
    @SuppressWarnings( "static-method" )
    public final boolean isNot() { return false; }

    /**
     *  Returns the current value of
     *  {@link #m_Attribute2}.
     *
     *  @return The attribute1.
     */
    public final boolean isAttribute2() { return m_Attribute2; }

    /**
     *  The program entry point.
     *
     *  @param  args    The command line arguments.
     */
    public static void main( final String... args )
    {
        try
        {
            // Does nothing ...
        }
        catch( final Throwable t )
        {
            //---* Handle any previously unhandled exceptions *----------------
            t.printStackTrace( err );
        }
    }   //  main()

    /**
     *  This is not the program entry point.
     */
    public static void main()
    {
        // Does nothing ...
    }   //  main()

    /**
     *  Sets a flag.
     *
     *  @param  flag    The flag.
     */
    public final void set( final boolean flag ) { m_Attribute2 = flag; }

    /**
     *  Sets a new value for
     *  {@link #m_Attribute1}.
     *
     *  @param attribute1 The new value for attribute1.
     */
    public final void setAttribute1( final String attribute1 )
    {
        if( null == attribute1 )
        {
            throw new NullArgumentException( "attribute1" );
        }

        m_Attribute1 = attribute1;
    }   //  setAttribute1()

    /**
     *  Sets a new value for
     *  {@link #m_Attribute2}.
     *
     *  @param attribute2 The new value for attribute1.
     */
    public final void setAttribute2( final boolean attribute2 )
    {
        m_Attribute2 = attribute2;
    }   //  setAttribute1()

    /**
     *  Sets a new value for
     *  {@link #m_Attribute3}.
     *
     *  @param attribute3   The new value for attribute1.
     */
    @SuppressWarnings( "unused" )
    private final void setAttribute3( final String attribute3 )
    {
        if( null == attribute3 )
        {
            throw new NullArgumentException( "attribute3" );
        }

        m_Attribute3 = attribute3;
    }   //  setAttribute3()

    /**
     *  Sets a new value for
     *  {@link #m_Attribute4}.
     *
     *  @param attribute4 The new value for attribute4.
     */
    public final void setAttribute4( final String attribute4 )
    {
        m_Attribute1 = requireNotEmptyArgument( attribute4, "attribute4" );
    }   //  setAttribute4()

    /**
     *  Sets a flag.
     *
     *  @param  name    The name of the flag.
     *  @param  flag    The flag.
     */
    public final void setFlag( @SuppressWarnings( "unused" ) final String name, final boolean flag ) { m_Attribute2 = flag; }

    /**
     *  A variant for toString().
     *
     *  @param  appendable  The appendable.
     *  @return The result from a call to
     *      {@link #toString()}.
     *  @throws IOException The call to
     *      {@link Appendable#append(CharSequence)}
     *      failed.
     */
    public final String toString( final Appendable appendable ) throws IOException
    {
        final var retValue = toString();
        appendable.append( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class Candidate

/*
 *  End of File
 */