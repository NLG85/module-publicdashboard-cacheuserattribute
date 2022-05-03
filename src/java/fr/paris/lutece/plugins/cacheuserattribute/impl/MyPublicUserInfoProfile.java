/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.cacheuserattribute.impl;

import java.security.GeneralSecurityException;
import java.util.Map;

import fr.paris.lutece.plugins.dashboard.service.PublicDashboardService;
import fr.paris.lutece.plugins.dashboard.service.PublicUserProfile;
import fr.paris.lutece.plugins.mylutece.modules.cacheuserattribute.service.CacheUserAttributeService;
import fr.paris.lutece.portal.service.security.RsaService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class MyPublicUserInfoProfile extends PublicUserProfile
{

    private MyPublicUserInfoProfile( UserProfileBuilder builder )
    {
        super( builder );
    }

    // builder
    public static class UserProfileBuilder extends PublicUserProfile.UserProfileBuilder
    {
        public UserProfileBuilder( String userid )
        {
            super( userid );
        }

        @Override
        public UserProfileBuilder withUserInfos( )
        {
            _mapUserInfos = searchName( _idUser );
            return this;
        }
    }

    private static Map<String, String> searchName( String key )
    {

        if ( AppPropertiesService.getPropertyBoolean( PublicDashboardService.PROPERTY_ENCRYPT, false ) )
        {
            try
            {
                key = RsaService.decryptRsa( key );
            }
            catch( GeneralSecurityException e )
            {
                AppLogService.error( "Cannot decrypt " + key, e );
            }
        }

        CacheUserAttributeService cacheService = new CacheUserAttributeService( );
        Map<String, String> map = cacheService.getCachedAttributes( key );

        return map;

    }

}
