/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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

import static java.lang.ClassLoader.getPlatformClassLoader;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.getAllStackTraces;
import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isNative;
import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isProtected;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isStrict;
import static java.lang.reflect.Modifier.isSynchronized;
import static java.lang.reflect.Modifier.isTransient;
import static java.lang.reflect.Modifier.isVolatile;
import static java.util.Arrays.stream;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.DEFAULT;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.NATIVE;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.element.Modifier.STRICTFP;
import static javax.lang.model.element.Modifier.SYNCHRONIZED;
import static javax.lang.model.element.Modifier.TRANSIENT;
import static javax.lang.model.element.Modifier.VOLATILE;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.lang.Objects.requireValidArgument;
import static org.tquadrat.foundation.util.StringUtils.capitalize;
import static org.tquadrat.foundation.util.StringUtils.decapitalize;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeKind;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.EnumSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.PropertyName;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.exception.UnexpectedExceptionError;
import org.tquadrat.foundation.exception.ValidationException;

/**
 *  <p>{@summary This class provides a bunch of helper methods that deal with the Java
 *  language itself and some related areas.} In general, they are wrapping
 *  somehow the introspection and the reflection frameworks.</p>
 *  <p>All methods of this class are static, so no instance of this class is
 *  allowed.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: JavaUtils.java 884 2021-03-22 18:02:51Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"ClassWithTooManyMethods", "OverlyComplexClass"} )
@ClassVersion( sourceVersion = "$Id: JavaUtils.java 884 2021-03-22 18:02:51Z tquadrat $" )
@UtilityClass
public final class JavaUtils
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The prefix for the name of an 'add' method: {@value}
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final String PREFIX_ADD = "add";

    /**
     *  The prefix for the name of a getter method: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String PREFIX_GET = "get";

    /**
     *  The prefix for the name of a getter method that returns a
     *  {@code boolean} value: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String PREFIX_IS = "is";

    /**
     *  The prefix for the name of a setter method: {@value}
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String PREFIX_SET = "set";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The flag that tracks the assertion on/off status for this package.
     */
    private static boolean m_AssertionOn;

    /**
     *  Unfortunately,
     *  {@link Class#forName(String, boolean, ClassLoader)}
     *  will not work for the classes of the primitive types. To enable
     *  {@link #loadClass(ClassLoader, String)}
     *  to return those classes, we use this table.
     */
    private static final Map<String,Class<?>> m_PrimitiveClasses;

    static
    {
        //---* Determine the assertion status *--------------------------------
        m_AssertionOn = false;
        /*
         * As the JUnit tests will always be executed with the flag
         * "-ea" (assertions enabled), this code sequence is not tested in all
         * branches.
         */
        //noinspection AssertWithSideEffects,PointlessBooleanExpression
        assert (m_AssertionOn = true) == true : "Assertion is switched off";

        //---* Create the table with the primitive type class objects *--------
        m_PrimitiveClasses = Map.of(
            "boolean", boolean.class,
            "byte", byte.class,
            "char", char.class,
            "double", double.class,
            "float", float.class,
            "int", int.class,
            "long", long.class,
            "short", short.class,
            "void", Void.class );
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private JavaUtils() { throw new PrivateConstructorForStaticClassCalledError( JavaUtils.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates the name for the getter method for the property with the given
     *  name. It will always be generated with '{@code get}', names for
     *  {@code boolean} properties (that usually do start with '{@code is}')
     *  are not generated.
     *
     *  @param  propertyName    The name of the property.
     *  @return The name for the getter of the respective property.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String composeGetterName( final String propertyName )
    {
        final var retValue = PREFIX_GET + capitalize( requireNotEmptyArgument( propertyName, "propertyName" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeGetterName()

    /**
     *  Creates the name for the setter method for the property with the given
     *  name.
     *
     *  @param  propertyName    The name of the property.
     *  @return The name for the setter of the respective property.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String composeSetterName( final String propertyName )
    {
        final var retValue = PREFIX_SET + capitalize( requireNotEmptyArgument( propertyName, "propertyName" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeSetterName()

    /**
     *  <p>{@summary This method will find the caller for the method that calls
     *  this one and returns the appropriate stack trace element.}</p>
     *  <p>The {@code offset} determines which caller's stack trace element
     *  will be returned:</p>
     *  <ol start="0">
     *      <li>this method</li>
     *      <li>the caller of this method</li>
     *      <li>the caller's caller</li>
     *      <li>… and so on</li>
     *  </ol>
     *
     *  @param  offset  The offset on the stack for the correct entry.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the stack trace element for the caller; will be empty if
     *      the offset is too high (higher than the number of call levels).
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Optional<StackTraceElement> findCaller( final int offset )
    {
        if( offset < 0 ) throw new ValidationException( "offset is negative" );

        //---* Retrieve the stack *--------------------------------------------
        final var stackTraceElements = currentThread().getStackTrace();
        final var len = stackTraceElements.length;

        //---* Search the stack *----------------------------------------------
        Optional<StackTraceElement> retValue = Optional.empty();
        if( offset <= len )
        {
            String className;
            String methodName;
            FindLoop: for( var i = 0; i < len; ++i )
            {
                /*
                 * This loop searches the stack until it will find the entry
                 * for this method on it. It assumes then that the next entry
                 * on the stack will belong to the caller for this method, and
                 * that one after it will be the caller's caller - and so on.
                 * The stack trace element for this method is not necessarily
                 * the first on the stack, as getStackTrace() is on it as well,
                 * and the exact index for this method depends on the
                 * implementation of the VM and/or the Java Runtime Library.
                 */
                className = stackTraceElements [i].getClassName();
                methodName = stackTraceElements [i].getMethodName();
                if( className.equals( JavaUtils.class.getName() ) && "findCaller".equals( methodName ) )
                {
                    if( (i + offset) < len )
                    {
                        retValue = Optional.of( stackTraceElements [i + offset] );
                    }
                    break FindLoop;
                }
            }   //  FindLoop:
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  findCaller()

    /**
     *  <p>{@summary Tries to identify the class with the {@code main()} method
     *  that started the application.}</p>
     *  <p>There are several reasons why this could fail:</p>
     *  <ul>
     *      <li>There is a
     *      {@link java.lang.SecurityManager}
     *      in place that forbids to access the necessary information.</li>
     *      <li>The {@code main} thread is already dead. This could happen
     *      with applications having a graphical user interface, or where the
     *      main class is mere starter for the real application threads.</li>
     *      <li>The code was not started as a program at all; this could be the
     *      fact for applets, or when it is started as a script.</li>
     *  </ul>
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the name of the main class.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Optional<String> findMainClass()
    {
        String mainClassName = null;
        try
        {
            //---* First, we will see if we are the main thread *--------------
            final var t = currentThread();
            StackTraceElement [] stackTrace;
            if( "main".equals( t.getName() ) )
            {
                //---* Search for the method main() *--------------------------
                stackTrace = t.getStackTrace();
                mainClassName = searchStackTrace( stackTrace, "main" );

                if( isNull( mainClassName ) )
                {
                    /*
                     * There is no main() method in the main thread; this can
                     * happen if we were triggered from a static block. So
                     * let's search for <clinit>.
                     */
                    mainClassName = searchStackTrace( stackTrace, "<clinit>" );
                }
            }

            //---* Not found yet, so we will search for the main thread *------
            if( isNull( mainClassName ) )
            {
                final var stackTraces = getAllStackTraces();
                ThreadSearchLoop: for( final var thread : stackTraces.keySet() )
                {
                    if( "main".equals( thread.getName() ) )
                    {
                        //---* Search for the method main() *------------------
                        stackTrace = stackTraces.get( thread );
                        mainClassName = searchStackTrace( stackTrace, "main" );
                        if( isNull( mainClassName ) )
                        {
                            /*
                             * There is no main() method in the main thread;
                             * this can happen if we were triggered from a
                             * static block. So let's search for <clinit>.
                             */
                            mainClassName = searchStackTrace( stackTrace, "<clinit>" );
                        }
                        if( nonNull( mainClassName ) ) break ThreadSearchLoop;
                    }
                }   //  ThreadSearchLoop:
            }
        }
        catch( @SuppressWarnings( "unused" ) final SecurityException e ) { /* Deliberately ignored */ }

        //---* Compose the return value *--------------------------------------
        final var retValue = Optional.ofNullable( mainClassName );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  findMainClass()

    /**
     *  <p>{@summary Retrieves the
     *  {@link ClassLoader}
     *  that loaded the class of the method that called the method that called
     *  this method.}</p>
     *  <p>If the class of the caller's caller was loaded by the Bootstrap
     *  classloader, the return value would be {@code null}, but this method
     *  will return the
     *  {@linkplain ClassLoader#getPlatformClassLoader() Platform classloader}
     *  instead.</p>
     *
     *  @return The caller's {@code ClassLoader}.
     *
     *  @see #findCaller(int)
     *
     *  @since 0.0.6
     */
    @API( status = STABLE, since = "0.0.6" )
    public static final ClassLoader getCallersClassLoader()
    {
        ClassLoader retValue = null;
        try
        {
            final var callersCaller = findCaller( 3 );
            @SuppressWarnings( "OptionalGetWithoutIsPresent" )
            final var callersClass = Class.forName( callersCaller.get().getClassName() );
            retValue = callersClass.getClassLoader();
            if( isNull( retValue ) ) retValue = getPlatformClassLoader();
        }
        catch( final NoSuchElementException e )
        {
            throw new UnexpectedExceptionError( "Caller's caller must be on the stack", e );
        }
        catch( final ClassNotFoundException e )
        {
            throw new UnexpectedExceptionError( "Caller's class must exist", e );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getCallersClassLoader()

    /**
     *  <p>{@summary Retrieves the location from where the code for the given
     *  class was loaded.}</p>
     *  <p>No location will be provided for classes from the Java run-time
     *  library (like
     *  {@link java.lang.String})
     *  or if there is a
     *  {@link java.lang.SecurityManager}
     *  in place that forbids this operation.</p>
     *  <p>Additionally, there are implementations of
     *  {@link java.lang.ClassLoader}
     *  that do not initialise the respective data structures
     *  appropriately.</p>
     *
     *  @param  c   The class to inspect.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the URL for the code source.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Optional<URL> getCodeSource( final Class<?> c )
    {
        Optional<URL> retValue = Optional.empty();
        try
        {
            final var protectionDomain = requireNonNullArgument( c, "c" ).getProtectionDomain();
            if( nonNull( protectionDomain ) )
            {
                final var codeSource = protectionDomain.getCodeSource();
                if( nonNull( codeSource ) )
                {
                    retValue = Optional.ofNullable( codeSource.getLocation() );
                }
            }
        }
        catch( @SuppressWarnings( "unused" ) final SecurityException e ) { /* Deliberately ignored */ }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getCodeSource()

    /**
     *  <p>{@summary Checks if the given element is an '<i>add</i>' method or
     *  not.} Such a method is a bit like a
     *  {@linkplain #isSetter(Element) setter method},
     *  but for
     *  {@link java.util.Collection Collection}
     *  instances or alike.</p>
     *  <p>Such an 'add' method is characterised by being public, not being
     *  default and not being static; it has a name starting with '{@code add}',
     *  it takes exactly one parameter, and it does not return any value.</p>
     *  <p>The remaining part of the name after '{@code add}' is taken as the
     *  name of the property.</p>
     *
     *  @param  element The element to check.
     *  @return {@code true} if the method is an 'add' method, {@code false}
     *      otherwise.
     *
     *  @see #isGetter(Element)
     *  @see #isSetter(Element)
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final boolean isAddMethod( final Element element )
    {
        //---* Check whether the element is a method at all *------------------
        var retValue = requireNonNullArgument( element, "element" ).getKind() == ElementKind.METHOD;
        if( retValue )
        {
            final var methodElement = (ExecutableElement) element;

            //---* Check if the method is public and not static *--------------
            final var modifiers = methodElement.getModifiers();
            if( (retValue = modifiers.contains( PUBLIC ) && !modifiers.contains( STATIC ) && !modifiers.contains( DEFAULT )) == true )
            {
                //---* Check the name *----------------------------------------
                final var name = methodElement.getSimpleName().toString();
                if( (retValue = name.startsWith( PREFIX_ADD )) == true )
                {
                    //---* Check if there is a property name *-----------------
                    final var pos = PREFIX_ADD.length();
                    retValue = (name.length() > pos) && Character.isUpperCase( name.charAt( pos ) );

                    //---* Check the number of parameters *--------------------
                    retValue &= (methodElement.getParameters().size() == 1);

                    //---* Check the return value *----------------------------
                    retValue &= methodElement.getReturnType() instanceof NoType;
                }
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isAddMethod()

    /**
     *  Checks whether JDK assertion is currently activated, meaning that the
     *  program was started with the command line flags {@code -ea} or
     *  {@code -enableassertions}. If assertions are activated for some
     *  selected packages only and {@code org.tquadrat.foundation.util} is not
     *  amongst these, or {@code org.tquadrat.foundation.util} is explicitly
     *  disabled with {@code -da} or {@code -disableassertions}, this method
     *  will return {@code false}. But even when it returns {@code true}, it is
     *  possible that assertions are still not activated for some packages.
     *
     *  @return {@code true} if assertions are activated for the
     *      package {@code org.tquadrat.util} and hopefully also for any other
     *      package, {@code false} otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isAssertionOn() { return m_AssertionOn; }

    /**
     *  Checks if the given method is the method
     *  {@link Object#equals(Object) equals()}
     *  as defined by the class {@code Object}. To be this method, it has to be
     *  public, it has to have the name 'equals', it has to take one argument
     *  of type {@code Object} and it will return a result of type
     *  {@code boolean}.
     *
     *  @param  method  The method to check.
     *  @return {@code true} if the method is the {@code equals()} method,
     *      {@code false} otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isEquals( final Method method )
    {
        //---* Check if the method is public and not static *------------------
        final var modifier = requireNonNullArgument( method, "method" ).getModifiers();
        var retValue = isPublic( modifier ) && !isStatic( modifier );

        //---* Check the name *------------------------------------------------
        retValue &= "equals".equals( method.getName() );

        //---* Check the return value *----------------------------------------
        retValue &= method.getReturnType().equals( boolean.class );

        //---* Check the parameters *------------------------------------------
        if( retValue )
        {
            final var parameterTypes = method.getParameterTypes();
            retValue = (parameterTypes.length == 1) && Object.class.equals( parameterTypes [0] );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isEquals()

    /**
     *  <p>{@summary Checks whether the given element is a <i>getter</i> method
     *  or not.}</p>
     *  <p>A getter method is public and not static, it has a name starting
     *  with &quot;{@code get}&quot;, it does not take any arguments, and it
     *  will return a value. In case the return value is of type
     *  {@code boolean}, the name may start with &quot;{@code is}&quot; instead
     *  of &quot;{@code get}&quot;.</p>
     *  <p>The remaining part of the name after &quot;{@code get}&quot; or
     *  &quot;{@code is}&quot; has to start with an uppercase letter; this is
     *  usually taken as the attribute's or property's name.</p>
     *  <p>For the method
     *  {@link Object#getClass()}
     *  (inherited by all classes from
     *  {@link Object}),
     *  this method will return {@code false}, as this is not a getter in the
     *  sense of the definition.</p>
     *
     *  @param  element  The element to check.
     *  @return {@code true} if the element is a getter method, {@code false}
     *      otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isGetter( final Element element )
    {
        //---* Check whether the element is a method at all *------------------
        var retValue = requireNonNullArgument( element, "element" ).getKind() == ElementKind.METHOD;
        if( retValue )
        {
            final var methodElement = (ExecutableElement) element;

            //---* Check if the method is public and not static *--------------
            final var modifiers = methodElement.getModifiers();
            if( (retValue = modifiers.contains( PUBLIC ) && !modifiers.contains( STATIC )) == true )
            {
                //---* Check the name *----------------------------------------
                final var name = methodElement.getSimpleName().toString();
                if( (retValue = !"getClass".equals( name )) == true )
                {
                    var pos = 0;
                    if( name.startsWith( PREFIX_IS ) )
                    {
                        //---* Check the return value *------------------------
                        final var returnMirror = methodElement.getReturnType();
                        retValue = !(returnMirror instanceof NoType) && (returnMirror.getKind() == TypeKind.BOOLEAN);
                        pos = PREFIX_IS.length();
                    }
                    else if( name.startsWith( PREFIX_GET ) )
                    {
                        //---* Check the return value *------------------------
                        retValue = !(methodElement.getReturnType() instanceof NoType);
                        pos = PREFIX_GET.length();
                    }
                    else
                    {
                        retValue = false;
                    }

                    if( retValue )
                    {
                        //---* Check if there is a property name *-------------
                        retValue = (name.length() > pos) && Character.isUpperCase( name.charAt( pos ) );

                        //---* Check the number of parameters *----------------
                        retValue = retValue && methodElement.getParameters().isEmpty();
                    }
                }
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isGetter()

    /**
     *  <p>{@summary Checks whether the given method is a <i>getter</i> method
     *  or not.}</p>
     *  <p>A getter method is public and not static, it has a name starting
     *  with &quot;{@code get}&quot;, it does not take any arguments, and it
     *  will return a value. In case the return value is of type
     *  {@code boolean}, the name may start with &quot;{@code is}&quot; instead
     *  of &quot;{@code get}&quot;.</p>
     *  <p>The remaining part of the name after &quot;{@code get}&quot; or
     *  &quot;{@code is}&quot; has to start with an uppercase letter; this is
     *  usually taken as the attribute's or property's name.</p>
     *  <p>For the method
     *  {@link Object#getClass()}
     *  (inherited by all classes from
     *  {@link Object}),
     *  this method will return {@code false}, as this is not a getter in the
     *  sense of the definition.</p>
     *
     *  @param  method  The method to check.
     *  @return {@code true} if the method is a getter, {@code false}
     *      otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isGetter( final Method method )
    {
        //---* Check if the method is public and not static *------------------
        final var modifier = requireNonNullArgument( method, "method" ).getModifiers();
        var retValue = isPublic( modifier ) && !isStatic( modifier );

        //---* Check the number of parameters *--------------------------------
        retValue = retValue && (method.getParameterTypes().length == 0);

        //---* Check the name *------------------------------------------------
        if( retValue )
        {
            final var name = method.getName();
            retValue = !"getClass".equals( name );

            if( retValue )
            {
                var pos = Integer.MAX_VALUE;
                if( name.startsWith( PREFIX_IS ) )
                {
                    retValue = method.getReturnType().equals( boolean.class );
                    pos = PREFIX_IS.length();
                }
                else if( name.startsWith( PREFIX_GET ) )
                {
                    //---* Check the return value *----------------------------
                    retValue = !method.getReturnType().equals( void.class );
                    pos = PREFIX_GET.length();
                }
                else
                {
                    retValue = false;
                }

                //---* Check if there is a property name *---------------------
                retValue = retValue && (name.length() > pos) && Character.isUpperCase( name.charAt( pos ) );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isGetter()

    /**
     *  Checks if the given method is the method
     *  {@link Object#hashCode() hashCode()}
     *  as defined by the class {@code Object}. To be this method, it has to be
     *  public, it has to have the name 'hashCode', it does not take any
     *  argument and it will return a result of type {@code integer}.
     *
     *  @param  method  The method to check.
     *  @return {@code true} if the method is the {@code hashCode()}
     *      method, {@code false} otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isHashCode( final Method method )
    {
        //---* Check if the method is public and not static *------------------
        final var modifier = requireNonNullArgument( method, "method" ).getModifiers();
        var retValue = isPublic( modifier ) && !isStatic( modifier );

        //---* Check the name *------------------------------------------------
        retValue &= "hashCode".equals( method.getName() );

        //---* Check the return value *----------------------------------------
        retValue &= method.getReturnType().equals( int.class );

        //---* Check the number of parameters *--------------------------------
        retValue &= (method.getParameterTypes().length == 0);

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isHashCode()

    /**
     *  Checks if the given method is a {@code main()} method or not. A
     *  {@code main()} method is public and static, it has the name
     *  &quot;main&quot;, it takes exactly one parameter of type
     *  {@code String []}, and it does not return any value.
     *
     *  @param  method  The method to check.
     *  @return {@code true} if the method is a {@code main()} method,
     *      {@code false} otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isMain( final Method method )
    {
        //---* Check if the method is public and static *----------------------
        final var modifier = requireNonNullArgument( method, "method" ).getModifiers();
        var retValue = isPublic( modifier ) && isStatic( modifier );

        //---* Check the name *------------------------------------------------
        retValue &= "main".equals( method.getName() );

        //---* Check the return value *----------------------------------------
        retValue &= method.getReturnType().equals( void.class );

        //---* Check the number of parameters *--------------------------------
        if( retValue )
        {
            final var parameterTypes = method.getParameterTypes();
            retValue = (parameterTypes.length == 1) && String [].class.equals( parameterTypes [0] );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isMain()

    /**
     *  <p>{@summary Checks if the given element is a setter method or not.} A
     *  setter method is public, it has a name starting with 'set', it takes
     *  exactly one parameter, and it does not return any value.</p>
     *  <p>The remaining part of the name after 'set' is taken as the
     *  attribute's name.</p>
     *
     *  @param  element The element to check.
     *  @return {@code true} if the method is a setter, {@code false}
     *      otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isSetter( final Element element )
    {
        //---* Check whether the element is a method at all *------------------
        var retValue = requireNonNullArgument( element, "element" ).getKind() == ElementKind.METHOD;
        if( retValue )
        {
            final var methodElement = (ExecutableElement) element;

            //---* Check if the method is public and not static *--------------
            final var modifiers = methodElement.getModifiers();
            if( (retValue = modifiers.contains( PUBLIC ) && !modifiers.contains( STATIC )) == true )
            {
                //---* Check the name *----------------------------------------
                final var name = methodElement.getSimpleName().toString();
                if( (retValue = name.startsWith( PREFIX_SET )) == true )
                {
                    //---* Check if there is a property name *-----------------
                    final var pos = PREFIX_SET.length();
                    retValue = (name.length() > pos) && Character.isUpperCase( name.charAt( pos ) );

                    //---* Check the number of parameters *--------------------
                    retValue &= (methodElement.getParameters().size() == 1);

                    //---* Check the return value *----------------------------
                    retValue &= methodElement.getReturnType() instanceof NoType;
                }
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isSetter()

    /**
     *  Checks if the given method is a setter method or not. A setter method
     *  is public, it has a name starting with 'set', it takes exactly one
     *  parameter, and it does not return any value.<br>
     *  <br>The remaining part of the name after 'set' is taken as the
     *  attribute's name.
     *
     *  @param  method  The method to check.
     *  @return {@code true} if the method is a setter, {@code false}
     *      otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isSetter( final Method method )
    {
        //---* Check if the method is public and not static *------------------
        final var modifier = requireNonNullArgument( method, "method" ).getModifiers();
        var retValue = isPublic( modifier ) && !isStatic( modifier );

        //---* Check the name *------------------------------------------------
        if( retValue )
        {
            final var name = method.getName();
            retValue = name.startsWith( PREFIX_SET );

            //---* Check if there is a property name *-------------------------
            final var pos = PREFIX_SET.length();
            retValue &= (name.length() > pos) && Character.isUpperCase( name.charAt( pos ) );
        }

        //---* Check the number of parameters *--------------------------------
        retValue &= (method.getParameterTypes().length == 1);

        //---* Check the return value *----------------------------------------
        retValue &= method.getReturnType().equals( void.class );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isSetter()

    /**
     *  Checks if the given method is the method
     *  {@link Object#toString() toString()}
     *  as defined by the class {@code Object}. To be this method, it has to be
     *  public, it has to have the name 'toString', it does not take any
     *  argument and it returns a result of type
     *  {@link String}.
     *
     *  @param  method  The method to check.
     *  @return {@code true} if the method is the {@code toString()}
     *      method, {@code false} otherwise.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isToString( final Method method )
    {
        //---* Check if the method is public and not static *------------------
        final var modifier = requireNonNullArgument( method, "method" ).getModifiers();
        var retValue = isPublic( modifier ) && !isStatic( modifier );

        //---* Check the name *------------------------------------------------
        retValue &= "toString".equals( method.getName() );

        //---* Check the return value *----------------------------------------
        retValue &= method.getReturnType().equals( String.class );

        //---* Check the number of parameters *--------------------------------
        retValue &= (method.getParameterTypes().length == 0);

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isToString()

    /**
     *  Checks whether the given String is a valid Java name.<br>
     *  <br>This method will return {@code true} for <i>restricted
     *  keywords</i>, but not for {@code var}. For a single underscore
     *  (&quot;{@code _}&quot;), it will return {@code false}.<br>
     *  <br>The restricted keywords are
     *  <ul>
     *  <li>{@code exports}</li>
     *  <li>{@code module}</li>
     *  <li>{@code open}</li>
     *  <li>{@code opens}</li>
     *  <li>{@code provides}</li>
     *  <li>{@code requires}</li>
     *  <li>{@code to}</li>
     *  <li>{@code transitive}</li>
     *  <li>{@code uses}</li>
     *  <li>{@code with}</li>
     *  </ul>
     *  All these are used in a {@code module-info.java} file.
     *
     *  @param  name   The String to check.
     *  @return {@code true} if the given String is a valid name for the Java
     *      language, {@code false} otherwise.
     *
     *  @see javax.lang.model.SourceVersion#isName(CharSequence, SourceVersion)
     *  @see javax.lang.model.SourceVersion#isIdentifier(CharSequence)
     *  @see javax.lang.model.SourceVersion#isKeyword(CharSequence, SourceVersion)
     *  @see javax.lang.model.SourceVersion#latest()
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final boolean isValidName( final CharSequence name )
    {
        final var retValue = SourceVersion.isName( requireNotEmptyArgument( name, "name" ) )
            && !name.equals( "var" );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isValidName()

    /**
     *  Loads the class with the given name, using the instance of
     *  {@link ClassLoader}
     *  that loaded the caller's class, and returns that class. If no class
     *  with that name could be found by that {@code ClassLoader}, no exception
     *  will be thrown; instead this method will return an empty
     *  {@link Optional}
     *  instance.<br>
     *  <br>If not loaded and initialised before, the loaded class is not yet
     *  initialised. That means that {@code static} code blocks have not been
     *  executed yet and class variables (static variables) are not
     *  initialised.<br>
     *  <br>Different from
     *  {@link Class#forName(String, boolean, ClassLoader)},
     *  this method is able to load the class objects for the primitive types,
     *  too.
     *
     *  @param  classname   The name of the class to load; may <i>not</i> be
     *      empty or {@code null}.
     *  @return The class wrapped in an
     *      {@link Optional}
     *      instance.
     *
     *  @see Class#forName(String)
     *  @see Class#forName(String, boolean, ClassLoader)
     *  @see Optional#isPresent()
     *  @see #getCallersClassLoader()
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Optional<Class<?>> loadClass( final String classname )
    {
        final var classLoader = getCallersClassLoader();
        final var retValue = loadClass( classLoader, classname );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  loadClass()

    /**
     *  Loads the class with the given name, using the given
     *  {@link ClassLoader}
     *  instance, and returns that class. If no class with that name could be
     *  found by that {@code ClassLoader}, no exception will be thrown; instead
     *  this method will return an empty
     *  {@link Optional}
     *  instance.<br>
     *  <br>If not loaded and initialised before, the loaded class is not yet
     *  initialised. That means that {@code static} code blocks have not been
     *  executed yet and class variables (static variables) are not
     *  initialised.<br>
     *  <br>Different from
     *  {@link Class#forName(String, boolean, ClassLoader)},
     *  this method is able to load the class objects for the primitive types,
     *  too.
     *
     *  @param  classLoader The class loader to use.
     *  @param  classname   The name of the class to load; may <i>not</i> be
     *      empty or {@code null}.
     *  @return The class wrapped in an
     *      {@link Optional}
     *      instance.
     *
     *  @see Class#forName(String)
     *  @see Class#forName(String, boolean, ClassLoader)
     *  @see Optional#isPresent()
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Optional<Class<?>> loadClass( final ClassLoader classLoader, final String classname )
    {
        var resultClass = m_PrimitiveClasses.get( requireNotEmptyArgument( classname, "classname" ) );
        if( isNull( resultClass ) )
        {
            try
            {
                resultClass = Class.forName( classname, false, requireNonNullArgument( classLoader, "classLoader" ) );
            }
            catch( @SuppressWarnings( "unused" ) final ClassNotFoundException e ) { /* Deliberately ignored */ }
        }

        //---* Create the return value *---------------------------------------
        final Optional<Class<?>> retValue = Optional.ofNullable( resultClass );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  loadClass()

    /**
     *  Loads the class with the given name, using the given
     *  {@link ClassLoader}
     *  instance, and returns that class, wrapped in an instance of
     *  {@link Optional}.
     *  If no class with that name could be found by that instance of
     *  {@code ClassLoader}, or if it does not implement the given
     *  interface/extend the given class, no exception will be thrown; instead
     *  this method will return an empty
     *  {@link Optional}
     *  instance.<br>
     *  <br>If not loaded and initialised before, the loaded class is not yet
     *  initialised. That means that {@code static} code blocks have not been
     *  executed yet and class variables (static variables) are not
     *  initialised.
     *
     *  @param  <T> The type of the interface/class that the returned class
     *      will implement/extend.
     *  @param  classLoader The class loader to use.
     *  @param  classname   The name of the class to load; may <i>not</i> be
     *      empty or {@code null}.
     *  @param  implementing    The interface/class that the returned class
     *      has to implement/extend.
     *  @return The class wrapped in an
     *      {@link Optional}
     *      instance.
     *
     *  @see Class#forName(String)
     *  @see Class#forName(String, boolean, ClassLoader)
     *  @see Optional#isPresent()
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final <T> Optional<Class<? extends T>> loadClass( final ClassLoader classLoader, final String classname, final Class<? extends T> implementing )
    {
        Class<? extends T> resultClass = null;
        try
        {
            final var c = Class.forName( requireNotEmptyArgument( classname, "classname" ), false, requireNonNullArgument( classLoader, "classLoader" ) );
            if( requireNonNullArgument( implementing, "implementing" ).isAssignableFrom( c ) )
            {
                resultClass = c.asSubclass( implementing );
            }
        }
        catch( @SuppressWarnings( "unused" ) final ClassNotFoundException e ) { /* Deliberately ignored */ }

        //---* Create the return value *---------------------------------------
        final Optional<Class<? extends T>> retValue = Optional.ofNullable( resultClass );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  loadClass()

    /**
     *  If no class with that name could be found by that instance of
     *  {@code ClassLoader}, or if it does not implement the given
     *  interface/extend the given class, no exception will be thrown; instead
     *  this method will return an empty
     *  {@link Optional}.<br>
     *  Loads the class with the given name, using the instance of
     *  {@link ClassLoader}
     *  that loaded the caller's class, and returns that class, wrapped in an
     *  instance of
     *  {@link Optional}.
     *  If no class with that name could be found by that instance of
     *  {@code ClassLoader}, or if it does not implement the given
     *  interface/extend the given class, no exception will be thrown; instead
     *  this method will return an empty
     *  {@link Optional}
     *  instance.<br>
     *  <br>If not loaded and initialised before, the loaded class is not yet
     *  initialised. That means that {@code static} code blocks have not been
     *  executed yet and class variables (static variables) are not
     *  initialised.
     *
     *  @param  <T> The type of the interface/class that the returned class
     *      will implement/extend.
     *  @param  classname   The name of the class to load; may <i>not</i> be
     *      empty or {@code null}.
     *  @param  implementing    The interface/class that the returned class
     *      has to implement/extend.
     *  @return The class wrapped in an
     *      {@link Optional}
     *      instance.
     *
     *  @see Class#forName(String)
     *  @see Class#forName(String, boolean, ClassLoader)
     *  @see Optional#isPresent()
     *  @see #getCallersClassLoader()
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final <T> Optional<Class<? extends T>> loadClass( final String classname, final Class<T> implementing )
    {
        final var classLoader = getCallersClassLoader();
        final var retValue = loadClass( classLoader, classname, implementing );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  loadClass()

    /**
     *  Retrieves the public getter for the property with the given name. If
     *  not {@code null}, the returned value will cause
     *  {@link #isGetter(Method)}
     *  to return {@code true}.
     *
     *  @param  beanClass   The class for the getter.
     *  @param  propertyName    The name of the property.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the getter method; will be empty if there is no public
     *      getter for the given property on the provided class.
     *
     *  @see #isGetter(Method)
     *  @see #retrieveGetter(Class, String, boolean)
     */
    public static final Optional<Method> retrieveGetter( final Class<?> beanClass, final String propertyName )
    {
        final var retValue = retrieveGetter( beanClass, propertyName, true );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveGetter()

    /**
     *  Retrieves the getter for the property with the given name.<br>
     *  <br>Usually, a getter method has to be public, but for some purposes,
     *  it may be package local, protected or even private. A method returned
     *  by a call to this method will not cause
     *  {@link #isGetter(Method)}
     *  to return {@code true} in all cases.
     *
     *  @param  beanClass   The class for the getter.
     *  @param  propertyName    The name of the property.
     *  @param  isPublic    {@code true} if the getter is required to be
     *      public, {@code false} otherwise.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the getter method; will be empty if there is no getter
     *      for the given property on the provided class.
     */
    @SuppressWarnings( "AssignmentToNull" )
    @API( status = STABLE, since = "0.0.5" )
    public static final Optional<Method> retrieveGetter( final Class<?> beanClass, final String propertyName, final boolean isPublic )
    {
        requireNonNullArgument( beanClass, "beanClass" );

        Method method = null;

        if( !"class".equals( requireNotEmptyArgument( propertyName, "propertyName" ) ) )
        {
            var getterName = composeGetterName( propertyName );

            //---* Retrieve a common getter *----------------------------------
            if( !isPublic )
            {
                try
                {
                    method = beanClass.getDeclaredMethod( getterName );
                }
                catch( @SuppressWarnings( "unused" ) final NoSuchMethodException e ) { /* Will be deliberately ignored */ }
            }

            if( isNull( method ) )
            {
                try
                {
                    method = beanClass.getMethod( getterName );
                }
                catch( @SuppressWarnings( "unused" ) final NoSuchMethodException e ) { /* Will be deliberately ignored */ }
            }

            if( isNull( method ) )
            {
                //---* Assume it is a boolean property ... *-------------------
                getterName = PREFIX_IS + capitalize( propertyName );
                if( !isPublic )
                {
                    try
                    {
                        method = beanClass.getDeclaredMethod( getterName );
                    }
                    catch( @SuppressWarnings( "unused" ) final NoSuchMethodException e ) { /* Will be deliberately ignored */ }
                }

                if( isNull( method ) )
                {
                    try
                    {
                        method = beanClass.getMethod( getterName );
                    }
                    catch( @SuppressWarnings( "unused" ) final NoSuchMethodException e ) { /* Will be deliberately ignored */ }
                }

                //---* Check the return type *---------------------------------
                if( nonNull( method ) )
                {
                    final var returnType = method.getReturnType();
                    if( !(returnType.equals( Boolean.class ) || returnType.equals( boolean.class )) )
                    {
                        method = null;
                    }
                }
            }
            else
            {
                //---* Check the return type *---------------------------------
                if( method.getReturnType().equals( void.class ) )
                {
                    method = null;
                }
            }

            if( nonNull( method ) )
            {
                //---* Check if the method is public and not static *----------
                final var modifier = method.getModifiers();
                if( isStatic( modifier ) )
                {
                    method = null;
                }
                else if( !isPublic( modifier ) )
                {
                    if( isPublic )
                    {
                        method = null;
                    }
                    else
                    {
                        /*
                         * Ensure that the non-public method can be accessed.
                         */
                        method.setAccessible( true );
                    }
                }
            }
        }

        //---* Compose the return value *--------------------------------------
        final var retValue = Optional.ofNullable( method );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveGetter()

    /**
     *  Returns all the getter methods from the given object.<br>
     *  <br><i>getter</i> methods ...
     *  <ul>
     *  <li>... are public</li>
     *  <li>... are <i>not</i> static</li>
     *  <li>... will return a value (are not {@code void}</li>
     *  <li>... do not take an argument</li>
     *  <li>... have a name that starts with &quot;{@code get}&quot;, followed
     *  by the name of the property that they return, with its first letter in
     *  uppercase (e.g. for the property &quot;{@code name}&quot;, get getter
     *  would be named &quot;{@code getName()}&quot;</li>
     *  <li>... may have a name that starts with &quot;{@code is}&quot; instead
     *  of &quot;{@code get}&quot; in case they return a {@code boolean}
     *  value.</li>
     *  </ul>
     *  <br>The method will ignore the method
     *  {@link Object#getClass()}
     *  that is present for each object instance.
     *
     *  @param  o   The object to inspect.
     *  @return The list of getters; it may be empty, but will never be
     *  {@code null}.
     *
     *  @see #isGetter(Method)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Method [] retrieveGetters( final Object o )
    {
        final var retValue =
            stream( requireNonNullArgument( o, "o" ).getClass().getMethods() )
                .filter( JavaUtils::isGetter )
                .toArray( Method []::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveGetters()

    /**
     *  <p>{@summary Retrieves the public method with the given signature from
     *  the given class.} The method will not throw an exception in case the
     *  method does not exist.
     *
     *  @param  sourceClass The class.
     *  @param  methodName  The name of the method.
     *  @param  args    The types of the method arguments.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the found instance of
     *      {@link Method}.
     *
     *  @see Class#getMethod(String, Class[])
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final Optional<Method> retrieveMethod( final Class<?> sourceClass, final String methodName, final Class<?>... args )
    {
        Optional<Method> retValue = Optional.empty();
        try
        {
            final var method = sourceClass.getMethod( methodName, args );
            retValue = Optional.of( method );
        }
        catch( final NoSuchMethodException e ) { /* Deliberately ignored */ }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveMethod()

    /**
     *  Retrieves the name of the property from the name of the given
     *  executable element for a method that is either a
     *  {@linkplain #isGetter(Element) getter},
     *  a
     *  {@linkplain #isSetter(Element) setter},
     *  or an
     *  {@linkplain #isAddMethod(Element) 'add'}
     *  method. Alternatively the method has an annotation that provides the
     *  name of the property.
     *
     *  @param  method  The method.
     *  @return The name of the property.
     */
    public static final String retrievePropertyName( final ExecutableElement method )
    {
        final String retValue;
        final var propertyNameAnnotation = requireNonNullArgument( method, "method" ).getAnnotation( PropertyName.class );
        if( nonNull( propertyNameAnnotation ) )
        {
            retValue = propertyNameAnnotation.value();
        }
        else
        {
            final var methodName = requireValidArgument( method, "method", v -> isGetter( v ) || isSetter( v ) || isAddMethod( v ), $ -> format( "'%s()' is not a valid type of method", method.getSimpleName() ) ).getSimpleName().toString();

            /*
             * We know that the method is either a getter, a setter or an 'add'
             * method. Therefore, we know also that the name starts either with
             * "get", "set", "add" or "is".
             */
            final var pos = methodName.startsWith( PREFIX_IS ) ? PREFIX_IS.length() : PREFIX_GET.length();
            retValue = decapitalize( methodName.substring( pos ) );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrievePropertyName()

    /**
     *  Retrieves the public setter for the property with the given name. If
     *  not {@code null}, the returned value will cause
     *  {@link #isSetter(Method)}
     *  to return {@code true}.
     *
     *  @param  beanClass   The class for the getter.
     *  @param  propertyName    The name of the property.
     *  @param  propertyType    The type of the property.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the setter method; will be empty if there is no public
     *      setter for the given property on the provided class.
     *
     *  @see #retrieveSetter(Class,String,Class,boolean)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Optional<Method> retrieveSetter( final Class<?> beanClass, final String propertyName, final Class<?> propertyType )
    {
        final var retValue = retrieveSetter( beanClass, propertyName, propertyType, true );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveSetter()

    /**
     *  Retrieves the setter for the property with the given name.<br>
     *  <br>For some purposes, non-public setters are quite useful; when
     *  {@code isPublic} is provided as {@code false}, this method will also
     *  return those setters.
     *
     *  @param  beanClass   The class for the getter.
     *  @param  propertyName    The name of the property.
     *  @param  propertyType    The type of the property.
     *  @param  isPublic    {@code true} if the setter is required to be
     *      public, {@code false} otherwise.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the setter method; will be empty if there is no setter
     *      for the given property on the provided class.
     *
     *  @see #isSetter(Method)
     */
    @SuppressWarnings( "AssignmentToNull" )
    @API( status = STABLE, since = "0.0.5" )
    public static final Optional<Method> retrieveSetter( final Class<?> beanClass, final String propertyName, final Class<?> propertyType, final boolean isPublic )
    {
        requireNonNullArgument( beanClass, "beanClass" );
        requireNonNullArgument( propertyType, "propertyType" );

        //---* Retrieve a common setter *--------------------------------------
        Method method = null;
        if( !isPublic )
        {
            try
            {
                /*
                 *  Class.getDeclaredMethod() will return any method with the
                 *  given name that is declared on the given class, no matter
                 *  whether it is public or private.
                 */
                method = beanClass.getDeclaredMethod( composeSetterName( propertyName ), propertyType );
            }
            catch( @SuppressWarnings( "unused" ) final NoSuchMethodException e ) { /* Will be deliberately ignored */ }
        }

        /*
         * method is null here, either because isPublic() is true, or the
         * public (or protected) method was not declared on the given class,
         * but inherited from the parent class (or it does not exist at all
         * ...)
         */
        if( isNull( method ) )
        {
            try
            {
                method = beanClass.getMethod( composeSetterName( propertyName ), propertyType );
            }
            catch( @SuppressWarnings( "unused" ) final NoSuchMethodException e ) { /* Will be deliberately ignored */ }
        }

        /*
         * We found a method with the right name, now have to perform some
         * checks on it.
         */
        if( nonNull( method ) )
        {
            //---* Check the return value *------------------------------------
            if( !method.getReturnType().equals( void.class ) )
            {
                method = null; // Wrong return type ...
            }
            else
            {
                //---* Check if the method is public and not static *----------
                final var modifier = method.getModifiers();
                if( isStatic( modifier ) )
                {
                    method = null; // Setters are never static
                }
                else if( !isPublic( modifier ) )
                {
                    if( isPublic )
                    {
                        method = null; // It has to be public, but it isn't
                    }
                    else
                    {
                        /*
                         * Ensure that the non-public method can be accessed.
                         */
                        method.setAccessible( true );
                    }
                }
            }
        }

        //---* Compose the return value *--------------------------------------
        final var retValue = Optional.ofNullable( method );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveSetter()

    /**
     *  Searches the given stack trace for references to the method with the
     *  given name and returns the name for the respective class.<br>
     *  <br>This is a helper method for
     *  {@link #findMainClass()}.
     *
     *  @param  stackTrace  The stack trace.
     *  @param  methodName  The name of the method to look for.
     *  @return The class name, or {@code null} if there is no reference to
     *      the given method in the stack trace.
     */
    @API( status = STABLE, since = "0.0.5" )
    private static final String searchStackTrace( final StackTraceElement [] stackTrace, final String methodName )
    {
        assert nonNull( stackTrace ) : "stackTrace is null";
        assert isNotEmpty( methodName ) : "methodName is empty or null";

        String retValue = null;
        String foundMethodName;
        for( var i = stackTrace.length; (i > 0) && isNull( retValue ); --i )
        {
            foundMethodName = stackTrace [i-1].getMethodName();
            if( foundMethodName.equals( methodName ) )
            {
                retValue = stackTrace [i-1].getClassName();
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  searchStackTrace()

    /**
     *  <p>{@summary Translates the integer value for the modifiers for a
     *  class, method or field as it is used by reflection to the {@code enum}
     *  values from
     *  {@link Modifier}.}</p>
     *  <p>The modifier
     *  {@link javax.lang.model.element.Modifier#DEFAULT}
     *  will not be in the return set as this cannot be retrieved at runtime,
     *  and the value
     *  {@link java.lang.reflect.Modifier#INTERFACE}
     *  does not exist as an {@code enum} value in
     *  {@link javax.lang.model.element.Modifier}.</p>
     *  <p>The modifiers {@code sealed} and {@code non-sealed}, belonging to
     *  the preview feature 'Sealed Classes' are defined in
     *  {@code javax.lang.model.element.Modifier}, but
     *  {@link Class#getModifiers()}
     *  will not return them, and they are not (yet) defined in
     *  {@link java.lang.reflect.Modifier}. Therefore they will not appear in
     *  the return set, too.</p>
     *
     *  @param  modifiers   The integer value for the modifiers.
     *  @return The modifier values.
     *
     *  @see javax.lang.model.element.Modifier#NON_SEALED
     *  @see javax.lang.model.element.Modifier#SEALED
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Set<Modifier> translateModifiers( final int modifiers )
    {
        final Set<Modifier> retValue = EnumSet.noneOf( Modifier.class );
        if( isAbstract( modifiers ) ) retValue.add( ABSTRACT );
        if( isFinal( modifiers ) ) retValue.add( FINAL );
        if( isNative( modifiers ) ) retValue.add( NATIVE );
        if( isPrivate( modifiers ) ) retValue.add( PRIVATE );
        if( isProtected( modifiers ) ) retValue.add( PROTECTED );
        if( isPublic( modifiers ) ) retValue.add( PUBLIC );
        if( isStatic( modifiers ) ) retValue.add( STATIC );
        if( isStrict( modifiers ) ) retValue.add( STRICTFP );
        if( isSynchronized( modifiers ) ) retValue.add( SYNCHRONIZED );
        if( isTransient( modifiers ) ) retValue.add( TRANSIENT );
        if( isVolatile( modifiers ) ) retValue.add( VOLATILE );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translateModifiers()
}
//  class JavaUtils

/*
 *  End of File
 */