/*
 * ============================================================================
 * Copyright © 2002-2026 by Thomas Thrien.
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

import static java.lang.System.getProperty;
import static java.lang.System.out;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.walkFileTree;
import static java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE;
import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;
import static java.util.Arrays.asList;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.PROPERTY_TEMPFOLDER;
import static org.tquadrat.foundation.lang.CommonConstants.PROPERTY_USER_NAME;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.splitString;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.PatternSyntaxException;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.lang.CommonConstants;

/**
 *  Some I/O, file, file system and network related helper and convenience
 *  methods.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: IOUtils.java 1228 2026-05-04 12:21:25Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "ClassWithTooManyMethods" )
@ClassVersion( sourceVersion = "$Id: IOUtils.java 1228 2026-05-04 12:21:25Z tquadrat $" )
@UtilityClass
public final class IOUtils
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  This implementation of an
     *  {@link Appendable}
     *  just swallows any data that is written to it, like the {@code /dev/null}
     *  device of a Unix or Linux machine, or {@code NUL:} on Windows. This class
     *  might be useful if an {@code Appendable} is required from the API, but
     *  not applicable from the application logic.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: IOUtils.java 1228 2026-05-04 12:21:25Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @SuppressWarnings( "PublicInnerClass" )
    @ClassVersion( sourceVersion = "$Id: IOUtils.java 1228 2026-05-04 12:21:25Z tquadrat $" )
    @API( status = STABLE, since = "0.1.0" )
    public static class NullAppendable implements Appendable
    {
            /*--------------*\
        ====** Constructors **=====================================================
            \*--------------*/
        /**
         *  Creates a new instance of {@code NullAppendable}.
         */
        public NullAppendable() { super(); }

            /*---------*\
        ====** Methods **==========================================================
            \*---------*/
        /**
         *  Appends the specified character sequence to this Appendable. In fact,
         *  it does nothing.
         *
         *  @param  csq {@inheritDoc}
         */
        @Override
        public final Appendable append( final CharSequence csq ) throws IOException { return this; }

        /**
         *  Appends a subsequence of the specified character sequence to this
         *  Appendable. In fact, it does nothing.
         *
         *  @param  csq {@inheritDoc}
         */
        @Override
        public final Appendable append( final CharSequence csq, final int start, final int end ) throws IOException { return this; }

        /**
         *  Appends the specified character to this Appendable; in fact, it does
         *  nothing.
         *
         *  @param  c   {@inheritDoc}
         */
        @Override
        public final Appendable append( final char c ) throws IOException { return this; }
    }
    //  class NullAppendable

    /**
     *  The default file attributes.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: IOUtils.java 1228 2026-05-04 12:21:25Z tquadrat $
     *  @since 0.0.6
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: IOUtils.java 1228 2026-05-04 12:21:25Z tquadrat $" )
    @UtilityClass
    private static final class PosixPermissions
    {
            /*------------------------*\
        ====** Static Initialisations **=======================================
            \*------------------------*/
        /**
         *  The default attributes for a temporary file.
         *
         *  @since 0.0.6
         */
        @API( status = INTERNAL, since = "0.0.6" )
        static final FileAttribute<Set<PosixFilePermission>> tempFilePermissions;

        /**
         *  The default attributes for a temporary folder.
         *
         *  @since 0.0.6
         */
        @API( status = INTERNAL, since = "0.0.6" )
        static final FileAttribute<Set<PosixFilePermission>> tempDirPermissions;

        static
        {
            tempFilePermissions = PosixFilePermissions.asFileAttribute( EnumSet.of( OWNER_READ, OWNER_WRITE ) );
            tempDirPermissions = PosixFilePermissions.asFileAttribute( EnumSet.of( OWNER_READ, OWNER_WRITE, OWNER_EXECUTE ) );
        }

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  No instance allowed for this class!
         */
        private PosixPermissions() { throw new PrivateConstructorForStaticClassCalledError( PosixPermissions.class ); }
    }
    //  class PosixPermissions

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  Some methods in this class need a buffer; the size of this buffer is
     *  defined here: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     *  The flag that indicates if the default file systems is POSIX compliant.
     *
     *  @since 0.0.6
     */
    @API( status = STABLE, since = "0.0.6" )
    public static final boolean DEFAULT_FILESYSTEM_IS_POSIX_COMPLIANT = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private IOUtils() { throw new PrivateConstructorForStaticClassCalledError( IOUtils.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Unconditionally closes an object instance of a class that implements
     *  the interface
     *  {@link AutoCloseable}.<br>
     *  Equivalent to
     *  {@link AutoCloseable#close()},
     *  except any exceptions will be ignored. This is typically used in
     *  {@code finally} blocks.<br>
     *  <br>Even after the introduction of {@code try-with-resources} with
     *  Java&nbsp;7, this method can be still helpful.
     *
     *  @param  closeable   The {@code AutoCloseable} instance to close, can be
     *      {@code null} or already closed.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void closeQuietly( final AutoCloseable closeable )
    {
        try
        {
            if( nonNull( closeable ) ) closeable.close();
        }
        catch( final Exception ignored ) { /* Deliberately ignored! */ }
    }   //  closeQuietly()

    /**
     *  <p>{@summary Creates a new directory by creating all nonexistent parent
     *  directories first.} Unlike the
     *  {@link #createDirectory(File,FileAttribute...)}
     *  method, an exception is not thrown if the directory could not be
     *  created because it already exists.</p>
     *  <p>The {@code attributes} parameter is optional to set
     *  {@linkplain FileAttribute file-attributes}
     *  atomically when creating the non-existent directories. Each file
     *  attribute is identified by its
     *  {@linkplain FileAttribute#name name}.
     *  If more than one attribute of the same name is included in the array
     *  then all but the last occurrence is ignored.</p>
     *  <p>If this method fails, then it may do so after creating some, but
     *  not all, of the parent directories.</p>
     *
     *  @param  dir The directory to create.
     *  @param  attributes  An optional list of file attributes to set
     *      atomically when creating the directory.
     *  @return The directory.
     *  @throws UnsupportedOperationException   The attributes array contains
     *      an attribute that cannot be set atomically when creating the
     *      directory.
     *  @throws java.nio.file.FileAlreadyExistsException    The {@code dir}
     *      exists but is not a directory <i>(optional specific exception)</i>.
     *  @throws IOException An I/O error occurred.
     *  @throws SecurityException   In the case a security manager is
     *      installed, the
     *      {@link SecurityManager#checkWrite(String) checkWrite()}
     *      method is invoked prior to attempting to create a directory and its
     *      {@link SecurityManager#checkRead(String) checkRead()}
     *      is invoked for each parent directory that is checked. If
     *      {@code dir} is not an absolute path then its
     *      {@link File#getAbsoluteFile()}
     *      method may need to be invoked to get its absolute path. This may
     *      invoke the security manager's
     *      {@link SecurityManager#checkPropertyAccess(String) checkPropertyAccess()}
     *      method to check access to the system property {@code user.dir}.
     *
     *  @see Files#createDirectories(Path, FileAttribute...)
     *  @see File#mkdirs()
     *
     *  @since 0.0.6
     */
    @API( status = STABLE, since = "0.0.6" )
    public static final File createDirectories( final File dir, final FileAttribute<?>... attributes ) throws IOException
    {
        final var path = Files.createDirectories( requireNonNullArgument( dir, "dir" ).toPath(), requireNonNullArgument( attributes, "attributes" ) );
        final var retValue = path.toFile();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createDirectories()

    /**
     *  <p>{@summary Creates a new directory.} The check for the existence of the file and
     *  the creation of the directory if it does not exist are a single
     *  operation that is atomic with respect to all other filesystem
     *  activities that might affect the directory. The
     *  {@link #createDirectories(File, FileAttribute...) createDirectories()}
     *  method should be used where it is required to create all nonexistent
     *  parent directories first.</p>
     *  <p>The {@code attributes} parameter is optional to set
     *  {@linkplain FileAttribute file-attributes}
     *  atomically when creating the directory. Each attribute is identified by
     *  its
     *  {@linkplain FileAttribute#name name}.
     *  If more than one attribute of the same name is included in the array
     *  then all but the last occurrence is ignored.</p>
     *
     *  @param  dir The directory to create.
     *  @param  attributes  An optional list of file attributes to set
     *      atomically when creating the directory
     *  @return The directory.
     *  @throws UnsupportedOperationException   The attributes array contains
     *      an attribute that cannot be set atomically when creating the
     *      directory.
     *  @throws java.nio.file.FileAlreadyExistsException    A directory could
     *      not otherwise be created because a file of that name already exists
     *      <i>(optional specific exception)</i>.
     *  @throws IOException An I/O error occurred or the parent directory does
     *      not exist.
     *  @throws SecurityException   In the case a security manager is
     *      installed, the
     *      {@link SecurityManager#checkWrite(String) checkWrite()}
     *      method is invoked to check write access to the new directory.
     *
     *  @since 0.0.6
     */
    @API( status = STABLE, since = "0.0.6" )
    public static File createDirectory( final File dir, final FileAttribute<?>... attributes ) throws IOException
    {
        final var path = Files.createDirectory( requireNonNullArgument( dir, "dir" ).toPath(), requireNonNullArgument( attributes, "attributes" ) );
        final var retValue = path.toFile();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createDirectory()

    /**
     *  <p>{@summary Creates a directory named after the account name of the
     *  current user (determined by the system property
     *  {@value CommonConstants#PROPERTY_USER_NAME})
     *  in the default {@code temp} folder, determined by the system property
     *  {@value CommonConstants#PROPERTY_TEMPFOLDER}.}</p>
     *  <p>The access rights are set for the current user only (for UNIX, it
     *  would be 700).</p>
     *  <p>The new directory will <i>not</i> be removed automatically after
     *  program termination.</p>
     *  <p>The class
     *  {@link File}
     *  provides a static method
     *  {@link File#createTempFile(String, String) createTempFile()}
     *  that creates a temporary file in the default temporary folder. As this
     *  may be not a problem on a single-user Windows system with default
     *  configuration, it will cause security problems on UNIX-like systems.
     *  Therefore, it is recommended, to use
     *  {@link File#createTempFile(String, String, File)}
     *  instead, like this:</p>
     *  <div class="source-container"><pre>final var tempFile = File.createTempFile( "PREFIX", "EXT", createTempDirectory() );</pre></div>
     *  <p>This will guarantee that the temporary files cannot be read by
     *  other users.</p>
     *
     *  @return The new temporary directory; it is guaranteed that the
     *      directory exists after the call to this method returned.
     *  @throws IOException Something has gone wrong.
     *
     *  @since 0.0.6
     */
    @API( status = STABLE, since = "0.0.6" )
    public static final File createTempDirectory() throws IOException
    {
        //---* Create the file object for the temp directory *-----------------
        final var tempDir = getSystemTempFolder();
        final var userTempDir = tempDir.resolve( getProperty( PROPERTY_USER_NAME ) );

        //---* Get the file attributes *---------------------------------------
        @SuppressWarnings( "ZeroLengthArrayAllocation" )
        final FileAttribute<?> [] attributes = DEFAULT_FILESYSTEM_IS_POSIX_COMPLIANT ? new FileAttribute [] {PosixPermissions.tempDirPermissions} : new FileAttribute [0];

        //---* Create the directory *------------------------------------------
        final var retValue = Files.createDirectories( userTempDir, attributes ).toFile();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createTempDirectory()

    /**
     *  <p>{@summary Creates a new temporary directory in the specified
     *  directory, using the given prefix to generate its name.}</p>
     *  <p>The details as to how the name of the directory is constructed is
     *  implementation dependent and therefore not specified. Where possible
     *  the {@code prefix} is used to construct candidate names.</p>
     *  <p>The {@code attributes} parameter is optional to set
     *  {@linkplain FileAttribute file-attributes}
     *  atomically when creating the directory. Each attribute is identified by
     *  its
     *  {@linkplain FileAttribute#name() name}.
     *  If more than one attribute of the same name is included in the array
     *  then all but the last occurrence is ignored.</p>
     *
     *  @param  dir The directory in which to create the temporary directory.
     *  @param  prefix  The prefix string to be used in generating the
     *      directory's name; may be {@code null}.
     *  @param  attributes  An optional list of file attributes to set
     *      atomically when creating the directory.
     *  @return The path to the newly created directory that did not exist
     *      before this method was invoked.
     *  @throws IllegalArgumentException    The prefix cannot be used to
     *      generate a candidate directory name.
     *  @throws UnsupportedOperationException   The attributes array contains
     *      an attribute that cannot be set atomically when creating the
     *      directory.
     *  @throws IOException An I/O error occurs or {@code dir} does not exist.
     *  @throws SecurityException   In the case a security manager is
     *      installed, the
     *      {@link SecurityManager#checkWrite(String) checkWrite()}
     *      method is invoked to check write access when creating the
     *      directory.
     *
     *  @see Files#createTempDirectory(Path,String,FileAttribute...)
     *
     *  @since 0.0.6
     */
    @API( status = STABLE, since = "0.0.6" )
    public static Path createTempDirectory( final File dir, final String prefix, final FileAttribute<?>... attributes ) throws IOException
    {
        return Files.createTempDirectory( requireNonNullArgument( dir, "dir" ).toPath(), prefix, requireNonNullArgument( attributes, "attributes" ) );
    }   //  createTempDirectory()

    /**
     *  <p>{@summary Creates a new directory in the default temporary-file
     *  directory, using the given prefix to generate its name.}</p>
     *  <p>This method works in exactly the manner specified by
     *  {@link #createTempDirectory(File,String,FileAttribute[])}
     *  method for the case that the {@code dir} parameter is the
     *  temporary-file directory.</p>
     *
     *  @param  prefix  The prefix string to be used in generating the
     *      directory's name; may be {@code null}.
     *  @param  attributes  An optional list of file attributes to set
     *      atomically when creating the directory.
     *  @return The
     *      {@link File}
     *      instance to the newly created directory that did not exist before
     *      this method was invoked.
     *  @throws IllegalArgumentException    The prefix cannot be used to
     *      generate a candidate directory name.
     *  @throws UnsupportedOperationException   The {@code attributes} array
     *      contains an attribute that cannot be set atomically when creating
     *      the directory.
     *  @throws IOException An I/O error occurred or the temporary-file
     *      directory does not exist.
     *  @throws SecurityException   In the case a security manager is
     *      installed, the
     *      {@link SecurityManager#checkWrite(String) checkWrite()}
     *      method is invoked to check write access when creating the
     *      directory.
     *
     *  @see Files#createTempDirectory(String, FileAttribute...)
     *
     *  @since 0.0.6
     */
    @API( status = STABLE, since = "0.0.6" )
    public static File createTempDirectory( final String prefix, final FileAttribute<?>... attributes ) throws IOException
    {
        final var path = Files.createTempDirectory( prefix, requireNonNullArgument( attributes, "attributes" ) );
        final var retValue = path.toFile();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createTempDirectory()

    /**
     *  <p>{@summary Deletes the folder (or file) that is determined by the
     *  given
     *  {@link Path}
     *  instance.} If the argument denotes a directory, the method will remove
     *  its contents first, recursively.</p>
     *
     *  @param  folder  The folder to remove; despite the name of the argument
     *      and the method, this can be also a plain file.
     *  @throws IOException A problem occurred when deleting the {@code Path}.
     */
    public static final void deleteFolder( final Path folder ) throws IOException
    {
        //noinspection AnonymousInnerClass,OverlyComplexAnonymousInnerClass
        walkFileTree( requireNonNullArgument( folder, "folder" ), new SimpleFileVisitor<>()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public final FileVisitResult postVisitDirectory( final Path directory, final IOException exception ) throws IOException
            {
                try
                {
                    delete( directory );
                }
                catch( final IOException e )
                {
                    if( nonNull( exception ) ) e.addSuppressed( exception );
                    throw e;
                }

                //---* Done *--------------------------------------------------
                return CONTINUE;
            }   //  postVisitDirectory()

            /**
             * {@inheritDoc}
             */
            @Override
            public final FileVisitResult visitFile( final Path file, final BasicFileAttributes attributes ) throws IOException
            {
                delete( file );

                //---* Done *--------------------------------------------------
                return CONTINUE;
            }   //  visitFile()
        } );
    }   //  deleteFolder()

    /**
     *  <p>{@summary Deletes the folder (or file) that is determined by the
     *  given
     *  {@link File}.
     *  instance.} If the argument denotes a directory, the method will remove
     *  its contents first, recursively.</p>
     *  <p>Calls
     *  {@link #deleteFolder(Path)}
     *  internally.</p>
     *
     *  @param  folder  The folder to remove; despite the name of the argument
     *      and the method, this can be also a plain file.
     *  @throws IOException A problem occurred when deleting the {@code Path}.
     */
    public static final void deleteFolder( final File folder ) throws IOException
    {
        deleteFolder( requireNonNullArgument( folder, "folder" ).toPath() );
    }   //  deleteFolder()

    /**
     *  <p>{@summary Calculates the check sum for the given file, using the
     *  algorithm with the given name.}</p>
     *  <p>If the name is one of</p>
     *  <ul>
     *      <li>CRC32</li>
     *      <li>Adler32</li>
     *  </ul>
     *  <p>the method uses
     *  {@link java.util.zip.CRC32}
     *  or
     *  {@link java.util.zip.Adler32}
     *  for the calculation, any other name is taken as the name for a
     *  {@linkplain MessageDigest};
     *  all JVMs know</p>
     *  <ul>
     *      <li>MD5</li>
     *      <li>SHA1</li>
     *  </ul>
     *  <p>others can be added by installing additional
     *  {@linkplain java.security.Provider security providers}.</p>
     *  <p>This method calls
     *  {@link #determineCheckSum(Path, String)}
     *  internally.</p>
     *
     *  @param  file    The file to process.
     *  @param  algorithm   The name for the algorithm to use for the check sum
     *      calculation.
     *  @return The check sum as a hex string.
     *  @throws IOException Problems to process the file.
     *  @throws NoSuchAlgorithmException    The provided algorithm does not
     *      exist or the provider for it is not installed properly.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String determineCheckSum( final File file, final String algorithm ) throws IOException, NoSuchAlgorithmException
    {
        final var retValue = determineCheckSum( requireNonNullArgument( file, "file" ).toPath(), algorithm );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineCheckSum()

    /**
     *  <p>{@summary Calculates the check sum for the given file, using the
     *  algorithm with the given name.}</p>
     *  <p>If the name is one of</p>
     *  <ul>
     *      <li>CRC32</li>
     *      <li>Adler32</li>
     *  </ul>
     *  <p>the method uses
     *  {@link java.util.zip.CRC32}
     *  or
     *  {@link java.util.zip.Adler32}
     *  for the calculation, any other name is taken as the name for a
     *  {@linkplain MessageDigest};
     *  all JVMs know</p>
     *  <ul>
     *      <li>MD5</li>
     *      <li>SHA1</li>
     *  </ul>
     *  <p>others can be added by installing additional
     *  {@linkplain java.security.Provider security providers}.</p>
     *
     *  @param  file    The file to process.
     *  @param  algorithm   The name for the algorithm to use for the check sum
     *      calculation.
     *  @return The check sum as a hex string.
     *  @throws IOException Problems to process the file.
     *  @throws NoSuchAlgorithmException    The provided algorithm does not
     *      exist or the provider for it is not installed properly.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String determineCheckSum( final Path file, final String algorithm ) throws IOException, NoSuchAlgorithmException
    {
        final var retValue = switch( requireNotEmptyArgument( algorithm, "algorithm" ) )
        {
            case "Adler32" -> Hash.create( file, new Adler32() ).toString();
            case "CRC32" -> Hash.create( file, new CRC32() ).toString();
            default -> Hash.create( file, MessageDigest.getInstance( algorithm ) ).toString();
        };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineCheckSum()

    /**
     *  Calculates the check sum for the given file, using provided check sum
     *  algorithm implementation.
     *
     *  @param  file    The file to process.
     *  @param  algorithm   The check sum algorithm to use for the check sum
     *      calculation.
     *  @return The check sum.
     *  @throws IOException Problems to process the file.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final long determineCheckSum( final File file, final Checksum algorithm ) throws IOException
    {
        final var retValue = determineCheckSum( requireNonNullArgument( file, "file" ).toPath(), algorithm );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineCheckSum()

    /**
     *  Calculates the check sum for the given file, using provided check sum
     *  algorithm implementation.
     *
     *  @param  file    The file to process.
     *  @param  algorithm   The check sum algorithm to use for the check sum
     *      calculation.
     *  @return The check sum.
     *  @throws IOException Problems to process the file.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final long determineCheckSum( final Path file, final Checksum algorithm ) throws IOException
    {
        final var retValue = Hash.create( file, algorithm )
            .number()
            .longValue();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineCheckSum()

    /**
     *  Calculates the check sum for the given file, using the provided
     *  {@link MessageDigest}.
     *
     *  @param  file    The file to process.
     *  @param  algorithm   The {@code MessageDigest} to use for the check sum
     *      calculation.
     *  @return The check sum as a byte array.
     *  @throws IOException Problems to process the file.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final byte [] determineCheckSum( final File file, final MessageDigest algorithm ) throws IOException
    {
        final var retValue = determineCheckSum( requireNonNullArgument( file, "file" ).toPath(), algorithm );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineCheckSum()

    /**
     *  Calculates the check sum for the given file, using the provided
     *  {@link MessageDigest}.
     *
     *  @param  file    The file to process.
     *  @param  algorithm   The {@code MessageDigest} to use for the check sum
     *      calculation.
     *  @return The check sum as a byte array.
     *  @throws IOException Problems to process the file.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final byte [] determineCheckSum( final Path file, final MessageDigest algorithm ) throws IOException
    {
        final var retValue = Hash.create( file, algorithm ).bytes();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  determineCheckSum()

    /**
     *  Returns an
     *  {@link Appendable}
     *  that just swallows any data that is written to it, like the
     *  {@code /dev/null} device of a Unix or Linux machine, or {@code NUL:}
     *  on Windows.
     *
     *  @return A null appendable.
     *
     *  @see NullAppendable
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Appendable getNullAppendable() { return new NullAppendable(); }

    /**
     *  Returns the
     *  {@link Path}
     *  object that represents the system's default temporary folder as
     *  specified in
     *  {@value CommonConstants#PROPERTY_TEMPFOLDER}.
     *
     *  @return The system temp folder.
     *
     *  @since 0.4.0
     */
    @API( status = STABLE, since = "0.4.0" )
    public static final Path getSystemTempFolder() { return Path.of( getProperty( PROPERTY_TEMPFOLDER ) ); }

    /**
     *  <p>{@summary Returns a
     *  {@link PathMatcher}
     *  for the
     *  {@linkplain FileSystems#getDefault() default filesystem}
     *  that performs match operations on the String representation of
     *  {@link Path}
     *  objects by interpreting a given pattern.}</p>
     *  <p>The {@code syntaxAndPattern} parameter identifies the syntax and the
     *  pattern and takes the form:</p>
     *  <blockquote><pre>
     *  [<i>syntax</i><b>:</b>]<i>pattern</i>
     *  </pre></blockquote>
     *  <p>where <i>syntax</i> is the non-empty name of the syntax, <i>pattern</i>
     *  is a possibly-empty pattern string, and {@code ':'} stands for
     *  itself.</p>
     *  <p>A
     *  {@link java.nio.file.FileSystem FileSystem}
     *  implementation supports the &quot;{@code glob}&quot; and
     *  &quot;{@code regex}&quot; syntaxes, and may support others. The value
     *  of the syntax component is compared without regard to case. If no
     *  syntax is provided explicitly, &quot;{@code glob}&quot; is assumed.</p>
     *  <p>When the syntax is &quot;{@code glob}&quot; then the {@code String}
     *  representation of the path is matched using a limited pattern language
     *  that resembles regular expressions but with a simpler syntax. For
     *  example:</p>
     *  <table class="striped"
     *         style="text-align:left; margin-left:2em">
     *      <caption style="display:none">Pattern Language</caption>
     *      <thead>
     *          <tr>
     *              <th scope="col">Example</th>
     *              <th scope="col">Description</th>
     *          </tr>
     *      </thead>
     *      <tbody>
     *          <tr>
     *              <th scope="row">{@code *.java}</th>
     *              <td>Matches a path that represents a file name ending in
     *              {@code .java}</td>
     *          </tr>
     *          <tr>
     *              <th scope="row">{@code *.*}</th>
     *              <td>Matches file names containing a dot</td>
     *          </tr>
     *          <tr>
     *              <th scope="row">{@code *.{java,class}}</th>
     *              <td>Matches file names ending with {@code .java} or
     *              {@code .class}</td>
     *          </tr>
     *          <tr>
     *              <th scope="row">{@code foo.?}</th>
     *              <td>Matches file names starting with {@code foo.} and a
     *              single character extension</td>
     *          </tr>
     *          <tr>
     *              <th scope="row"><code>&#47;home&#47;*&#47;*</code>
     *              <td>Matches <code>&#47;home&#47;gus&#47;data</code> on
     *              UNIX platforms</td>
     *          </tr>
     *          <tr>
     *              <th scope="row"><code>&#47;home&#47;**</code>
     *              <td>Matches <code>&#47;home&#47;gus</code> and
     *              <code>&#47;home&#47;gus&#47;data</code> on UNIX
     *              platforms</td>
     *          </tr>
     *          <tr>
     *              <th scope="row"><code>C:&#92;&#92;*</code>
     *              <td>Matches <code>C:&#92;foo</code> and
     *              <code>C:&#92;bar</code> on the Windows platform (note that
     *              the backslash is escaped; as a string literal in the Java
     *              Language the pattern would be
     *              <code>"C:&#92;&#92;&#92;&#92;*"</code>)</td>
     *          </tr>
     *      </tbody>
     *  </table>
     *  <p>The following rules are used to interpret glob patterns:</p>
     *  <ul>
     *      <li><p>The {@code *} character matches zero or more
     *      {@link Character characters}
     *      of a
     *      {@link Path#getName(int) name}
     *      component without crossing directory boundaries.</p></li>
     *      <li><p>The {@code **} characters matches zero or more
     *      {@link Character characters}
     *      crossing directory boundaries.</p></li>
     *      <li><p>The {@code ?} character matches exactly one character of a
     *      name component.</p></li>
     *      <li><p>The backslash character ({@code \}) is used to escape
     *      characters that would otherwise be interpreted as special
     *      characters. The expression &quot;{@code \\}&quot; matches a single
     *      backslash and &quot;<code>\{</code>&quot; matches a left brace for
     *      example.</p></li>
     *      <li><p>The {@code [ ]} characters are a <i>bracket expression</i>
     *      that match a single character of a name component out of a set of
     *      characters.</p>
     *      <p>For example, {@code [abc]} matches {@code "a"}, {@code "b"},
     *      or {@code "c"}.</p>
     *      <p>The hyphen ({@code -}) may be used to specify a range so
     *      {@code [a-z]} specifies a range that matches from {@code "a"} to
     *      {@code "z"} (inclusive).</p>
     *      <p>These forms can be mixed so [abce-g] matches {@code "a"}, {@code "b"},
     *      {@code "c"}, {@code "e"}, {@code "f"} or {@code "g"}. If the
     *      character after the {@code [} is a {@code !} then it is used for
     *      negation so {@code [!a-c]} matches any character <i>except</i>
     *      {@code "a"}, {@code "b"}, or {@code "c"}.</p>
     *      <p>Within a bracket expression the {@code *}, {@code ?} and
     *      {@code \} characters match themselves. The ({@code -}) character
     *      matches itself if it is the first character within the brackets, or
     *      the first character after the {@code !} if negating.</p></li>
     *      <li><p>The {@code { }} characters are a group of subpatterns, where
     *      the group matches if any subpattern in the group matches. The
     *      {@code ","} character is used to separate the subpatterns. Groups
     *      cannot be nested.</p></li>
     *      <li><p> Leading period<code>&#47;</code>dot characters in file name
     *      are treated as regular characters in match operations. For example,
     *      the {@code "*"} glob pattern matches file name {@code ".login"}.
     *      The
     *      {@link Files#isHidden}
     *      method may be used to test whether a file is considered
     *      hidden.</p></li>
     *      <li><p>All other characters match themselves in an implementation
     *      dependent manner. This includes characters representing any
     *      {@linkplain java.nio.file.FileSystem#getSeparator name-separators}.</p></li>
     *      <li><p>The matching of
     *      {@link Path#getRoot root}
     *      components is highly implementation-dependent and is not
     *      specified.</p></li>
     *  </ul>
     *  <p>When the syntax is "{@code regex}" then the pattern component is a
     *  regular expression as defined by the
     *  {@link java.util.regex.Pattern}
     *  class.</p>
     *  <p>For both the {@code glob} and {@code regex} syntaxes, the matching
     *  details, such as whether the matching is case-sensitive, are
     *  implementation-dependent and therefore not specified.</p>
     *
     *  @param  syntaxAndPattern    The syntax and pattern.
     *  @return A path matcher that may be used to match paths against the
     *      pattern.
     *  @throws PatternSyntaxException  The given pattern is invalid.
     *  @throws UnsupportedOperationException   The pattern syntax is not known
     *      to the implementation.
     *
     *  @see Files#newDirectoryStream(Path,String)
     *
     *  @since 0.25.3
     */
    @API( status = STABLE, since = "0.25.3" )
    public static final PathMatcher getPathMatcher( final CharSequence syntaxAndPattern )
    {
        final var parts = splitString( requireNonNullArgument( syntaxAndPattern, "syntaxAndPattern" ), ':' );
        final var fileSystem = FileSystems.getDefault();
        final var effectiveSyntaxAndPattern = new StringJoiner( ":" );
        if( parts.length == 1 ) effectiveSyntaxAndPattern.add( "glob" );
        for( final var p : parts ) effectiveSyntaxAndPattern.add( p );
        final var retValue = fileSystem.getPathMatcher( effectiveSyntaxAndPattern.toString() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getPathMatcher()

    /**
     *  <p>{@summary Returns
     *  {@link System#out}
     *  with a non-functional
     *  {@link OutputStream#close()}
     *  method.}</p>
     *  <p>Assume the following scenario:</p>
     *  <div class="source-container"><pre>&hellip;
     *  Optional&lt;File&gt; outputFile = &hellip;
     *  &hellip;
     *  PrintStream outputStream = outputFile.isPresent() ? new PrintStream( new FileOutputStream( outputFile.get() ) ) : IOUtils.getUncloseableOut();
     *  try( outputStream )
     *  {
     *      &#47;**
     *       * Print something ...
     *       *&#47;
     *       &hellip;
     *  }
     *  &hellip;</pre></div>
     *  <p>The output stream will be close at the end of the {@code try} block;
     *  this is desired in case of a
     *  {@link java.io.FileOutputStream FileOutputStream},
     *  but totally not wanted if the output stream is {@code System.out}.</p>
     *
     *  @return {@code System.out} without the {@code close()} method.
     */
    @SuppressWarnings( "ImplicitDefaultCharsetUsage" )
    @API( status = STABLE, since = "0.0.7" )
    public static final PrintStream getUncloseableOut()
    {
        @SuppressWarnings( {"AnonymousInnerClass", "UseOfSystemOutOrSystemErr"} )
        final var retValue = new PrintStream( out )
        {
            /**
             *  {@inheritDoc}
             */
            @Override
            public final void close() { /* Does nothing */ }
        };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getUncloseableOut()

    /**
     *  <p>{@summary Reads the complete content of the provided
     *  {@link Reader}
     *  into a
     *  {@link String}.}</p>
     *  <p>Obviously this method is feasible only for files with a limited
     *  size.</p>
     *
     *  @param  reader  The {@code Reader} instance.
     *  @return The content of the provided {@code Reader}.
     *  @throws IOException Problems on reading from the {@code Reader}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String loadToString( final Reader reader ) throws IOException
    {
        final var builder = new StringBuilder( DEFAULT_BUFFER_SIZE );
        final var buffer = new char [DEFAULT_BUFFER_SIZE];
        var bytesRead = requireNonNullArgument( reader, "reader" ).read( buffer );
        while( bytesRead > 0 )
        {
            builder.append( buffer, 0, bytesRead );
            bytesRead = reader.read( buffer );
        }
        final var retValue = builder.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  loadToString()

    /**
     *  <p>{@summary Parses a list of file name patterns to a list of
     *  {@link PathMatcher}
     *  instances.}</p>
     *  <p>The syntax for the patterns is described in the documentation for
     *  {@link #getPathMatcher(CharSequence) getPathMatcher()}.</p>
     *
     *  @param  patterns    The list of patterns.
     *  @return  The unmodifiable list of
     *      {@link PathMatcher}
     *      instances.
     */
    public static final Collection<PathMatcher> parseFilePatterns( final CharSequence... patterns )
    {
        final var retValue = parseFilePatterns( asList( requireNonNullArgument( patterns, "patterns" ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parseFilePatterns()

    /**
     *  <p>{@summary Parses a list of file name patterns to a list of
     *  {@link PathMatcher}
     *  instances.}</p>
     *  <p>The syntax for the patterns is described in the documentation for
     *  {@link #getPathMatcher(CharSequence) getPathMatcher()}.</p>
     *
     *  @param  patterns    The list of patterns.
     *  @return  The unmodifiable list of
     *      {@link PathMatcher}
     *      instances.
     */
    public static final Collection<PathMatcher> parseFilePatterns( final Collection<? extends CharSequence> patterns )
    {
        final Collection<PathMatcher> buffer = new ArrayList<>();
        for( final var pattern : requireNonNullArgument( patterns, "patterns" ) )
        {
            if( isNull( pattern ) ) throw new IllegalArgumentException( "A pattern is null" );
            try
            {
                final var matcher = getPathMatcher( pattern.toString() );
                buffer.add( matcher );
            }
            catch( final PatternSyntaxException | UnsupportedOperationException e )
            {
                throw new IllegalArgumentException( "Invalid Pattern: %s".formatted( pattern ), e );
            }
        }

        final var retValue = List.copyOf( buffer );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parseFilePatterns()
}
//  class IOUtils

/*
 *  End of File
 */