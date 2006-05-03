package org.apache.maven.plugin.pmd;

/*
 * Copyright 2005-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.util.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author <a href="mailto:oching@apache.org">Maria Odea Ching</a>
 */
public class PmdReportTest
    extends AbstractMojoTestCase
{
    protected void setUp()
        throws Exception
    {
        super.setUp();
    }

    public void testDefaultConfiguration()
        throws Exception
    {
        FileUtils.copyDirectoryStructure(
            new File( getBasedir(), "src/test/resources/unit/default-configuration/jxr-files" ),
            new File( getBasedir(), "target/test/unit/default-configuration/target/site" ) );

        File testPom = new File( getBasedir(),
                                 "src/test/resources/unit/default-configuration/default-configuration-plugin-config.xml" );
        PmdReport mojo = (PmdReport) lookupMojo( "pmd", testPom );
        mojo.execute();

        //check if the PMD files were generated
        File generatedFile = new File( getBasedir(), "target/test/unit/default-configuration/target/pmd.xml" );
        assertTrue( FileUtils.fileExists( generatedFile.getAbsolutePath() ) );

        generatedFile = new File( getBasedir(), "target/test/unit/default-configuration/target/basic.xml" );
        assertTrue( FileUtils.fileExists( generatedFile.getAbsolutePath() ) );

        generatedFile = new File( getBasedir(), "target/test/unit/default-configuration/target/imports.xml" );
        assertTrue( FileUtils.fileExists( generatedFile.getAbsolutePath() ) );

        generatedFile = new File( getBasedir(), "target/test/unit/default-configuration/target/unusedcode.xml" );
        assertTrue( FileUtils.fileExists( generatedFile.getAbsolutePath() ) );

        generatedFile = new File( getBasedir(), "target/test/unit/default-configuration/target/site/pmd.html" );
        assertTrue( FileUtils.fileExists( generatedFile.getAbsolutePath() ) );

        //check if there's a link to the JXR files
        String str =
            readFile( new File( getBasedir(), "target/test/unit/default-configuration/target/site/pmd.html" ) );
        assertTrue( str.toLowerCase().indexOf( "/xref/def/configuration/AppSample.html#31".toLowerCase() ) != -1 );

        str = readFile( new File( getBasedir(), "target/test/unit/default-configuration/target/site/pmd.html" ) );
        assertTrue( str.toLowerCase().indexOf( "/xref/def/configuration/App.html#12".toLowerCase() ) != -1 );

    }

    /**
     * With custom rulesets
     *
     * @throws Exception
     */
    public void testCustomConfiguration()
        throws Exception
    {
        File testPom = new File( getBasedir(),
                                 "src/test/resources/unit/custom-configuration/custom-configuration-plugin-config.xml" );
        PmdReport mojo = (PmdReport) lookupMojo( "pmd", testPom );
        mojo.execute();

        //check the generated files
        File generatedFile = new File( getBasedir(), "target/test/unit/custom-configuration/target/pmd.csv" );
        assertTrue( FileUtils.fileExists( generatedFile.getAbsolutePath() ) );

        generatedFile = new File( getBasedir(), "target/test/unit/custom-configuration/target/custom.xml" );
        assertTrue( FileUtils.fileExists( generatedFile.getAbsolutePath() ) );

        generatedFile = new File( getBasedir(), "target/test/unit/custom-configuration/target/site/pmd.html" );
        assertTrue( FileUtils.fileExists( generatedFile.getAbsolutePath() ) );

        //check if custom ruleset was applied
        String str = readFile( new File( getBasedir(), "target/test/unit/custom-configuration/target/site/pmd.html" ) );
        assertTrue( str.toLowerCase().indexOf( "Avoid using if statements without curly braces".toLowerCase() ) != -1 );

        str = readFile( new File( getBasedir(), "target/test/unit/custom-configuration/target/site/pmd.html" ) );
        assertTrue(
            str.toLowerCase().indexOf( "Avoid using if...else statements without curly braces".toLowerCase() ) != -1 );

    }

    public void testInvalidFormat()
        throws Exception
    {
        try
        {
            File testPom =
                new File( getBasedir(), "src/test/resources/unit/invalid-format/invalid-format-plugin-config.xml" );
            PmdReport mojo = (PmdReport) lookupMojo( "pmd", testPom );
            mojo.execute();

            fail( "Must throw MavenReportException." );
        }
        catch ( Exception e )
        {
            assertTrue( true );
        }
    }

    protected void tearDown()
        throws Exception
    {

    }

    /**
     * Read the contents of the specified file object into a string
     *
     * @param file the file to be read
     * @return a String object that contains the contents of the file
     * @throws java.io.IOException
     */
    private String readFile( File file )
        throws IOException
    {
        String str = "", strTmp = "";
        BufferedReader in = new BufferedReader( new FileReader( file ) );

        while ( ( strTmp = in.readLine() ) != null )
        {
            str = str + " " + strTmp;
        }
        in.close();

        return str;
    }


}
