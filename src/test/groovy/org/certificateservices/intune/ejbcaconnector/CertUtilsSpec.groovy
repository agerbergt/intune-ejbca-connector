package org.certificateservices.intune.ejbcaconnector

import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.util.encoders.Base64
import spock.lang.Specification

class CertUtilsSpec extends Specification {
    static String PKCS10Request = "MIIYuDCCF6ACAQAwNDEyMDAGA1UEAwwpYWRtaW5AZW50ZXJwcmlzZWphdmFiZWFucy5vbm1pY3Jvc29mdC5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDFtguargsInKSc/ZGdAGL8n1FZvXwyYfbgIC6Z0HZ3HeOM7PyF4mHlnXjPoaN/rQ5Wm/c51YSdEBbXAI4ZyK8Fk7Ule18BTIHeOkyteA4pnLTZOb+uFbNvBA9d3R5XcXp8Aj/ltuclD0YzpIOKsKltCa6+9qGNdfOE+YgDqFeG1Xfqz8EwbiovuREaMiL6ygcChnZuxjZsIIFcDe3XOIy7ftcfArwY9BJh6kvN4xXdmBrRcy3GJMIYTq97LCtTEfB3mHao1rr2HR1acl7tL9w+frE+7uM+U8oM4tYmbPbu1VDCHvfZNM0zlsjhC0ntbm88pjO5PdDvjWSsWzPmSkjpAgMBAAGgghY9MDwGCisGAQQBgjcNAgExLjAsHhwAVgBhAGwAaQBkAGkAdAB5AFAAZQByAGkAbwBkHgwATQBvAG4AdABoAHMwPAYKKwYBBAGCNw0CATEuMCweJgBWAGEAbABpAGQAaQB0AHkAUABlAHIAaQBvAGQAVQBuAGkAdABzHgIANjCBpwYJKoZIhvcNAQkOMYGZMIGWMBYGA1UdJQEB/wQMMAoGCCsGAQUFBwMCMA4GA1UdDwEB/wQEAwIFoDAMBgNVHRMBAf8EAjAAMBUGCSsGAQQBgjcVCAQIMAagBKACDAAwRwYDVR0RAQH/BD0wO6A5BgorBgEEAYI3FAIDoCsMKWFkbWluQGVudGVycHJpc2VqYXZhYmVhbnMub25taWNyb3NvZnQuY29tMIIVEwYJKoZIhvcNAQkHMYIVBBOCFQBQRU5sY25SRmJuSnZiR3hVYjJ0bGJqNDhSR0YwWVQ0OFEyVnlkRVZ1Y205c2JFTm9ZV3hzWlc1blpUNU5TVWxKU0VGWlNrdHZXa2xvZG1OT1FWRmpSRzlKU1VsRVZFTkRRMEZyUTBGUlFYaG5aMGhCVFVsSlFuWkJTVUpCUkVOQ2IzcERRbWw2UlV4TlFXdEhRVEZWUlVKb1RVTldWazE0UlhwQlVrSm5UbFpDUVdkVVEyeGthR015YUhCaWJXUXdZakkwZUVWRVFVOUNaMDVXUWtGalZFSXhTbXhhUnpGMlltMVJlRWhxUVdOQ1owNVdRa0Z2VkVaVk1YQlpNMHAyWXpJNWJXUkRRa1JpTTBwM1lqTkthR1JIYkhaaWFrVldUVUpOUjBFeFZVVkRlRTFOVkZkc2FtTnRPWHBpTWxvd1NVVnNWVTFTTkhkSVFWbEVWbEZSUkVWNFZrNWhWMDU1WWpOT2RscHVVV2RUVmxGblZrVjRWRWxGVGtKSlJGRkRSWGhaUVVGaFRTdFVkeXRFUVhreksyTnNjMEZCUVVGQ2IzbzBkMFJSV1VwTGIxcEphSFpqVGtGUlJVaE5RVUZGWjJkRlFWUjFaRk5vTmtkaGVUSkpTbWx5WVVKNE1tTnRhMWh2VjJseE9FNXNkWEZyZVhaUFlrSmFRVlE1VkZreFFYWTFNMjVCUmt4R1IzaDRjemd5YkZkUk5tNVBVbTFsVFZOSmFEZ3hkR2cxYkZkSVdXSlJlRWN2ZVhCbVNVMDJlR042U2xoNksxRkdRemgyZDNwdmFrZHBVbmR6YWl0QmFHd3dXVXRoUmtkbVlXUTJRWEJCUzJZM01qaHRkbkZqSzFVelRUQlVVRmhGZUVvMFltdDBhMDkxYzFFclRFa3dRVmxhUjFveFRXMTZRMG81UlV0VFlrZDJLMHRHYUN0ek5VVjJjR012YUROb1FUSnpTa3BIWkhCd1ZFazJPQzlhU1ZOMGFuSkVka3BaUldneE9VRk5URWg2YWtaaFJFUkxRWHBaSzNCNk9YQnJjVFZHZUV0UmIxa3lPRlZPY1V4TFVFOXhjR1pNVTJsa1VVSkVNbXBCY2pKc1JHcHhkSEIxVEUxUVdGTjBhMHB2SzJOYU5tTkZObWhvVlc1TmFGRnFZVlZWYlVkck1YQlZTRFozUkZGSVUybDJWemRVUzJocVltTmFSR3RyV1hNNWFrTkRRbW8wUjBOVGNVZFRTV0l6UkZGRlNFRlVRV1JDWjJ4bmFHdG5RbHBSVFVWQlUyOUZSVXBZZFhaalJXbFNNV1YwU0hKSFJIQk9jMEZhTlhWQloyZFpVVkF6VjNVemNVSk9WVWx5VFhoR2IzRjFjRVpHUzNJdmJIaHdUSFYzVVVwT2IzVlBTbmxqSzBkQmFqaFZOR2NyTjNSVFJsZERWRVo1YlhoQ2NpdEJaVFZWSzFKYVQwZEJTVWQ1YlU5S2JHcFZXSGxTUjFwWFMwODVibVo1TkhoSlRtbE9TblpKTUV0RWNYWXZWbmw0WlhoTFUwTlFVak5pT1RSelEwbEZlQzluUm13elluUllTazV1VDJrMlNHSXdka0Y2TVZCSE9IbFdVbnBUUjBSVFJFOWFZa2xTV2pKS05DOUZjRU54VkRkWU9YbzNWbGRhYm1GdlltWldZa2t2YXl0bmJTczFUMDlsYTA0NGJXRjNhRXhCVWsxak5Yb3hjSGcwY0VwVWJHcGtSbVJPVDJ4QmEwVlJlRVEyTWpZdk9XbHRXRUpSUzFKTU9XYzFWMUZRVEhScVYxbEZaakZSVGxsS1RVczBORll5YUc5VE9XSnBPRTlYY0ZCWlMwbE1XalZuWldWVFFVTTJTR3BzYm14NlRVMWthVXNyTDJ4MldFMW1PVUozUXpWUk9UUkVWSEp5UVZoSFNVcHZaMDE2ZUZKNk5tNDNWVlZpSzBoSk9EazVjWEp3V0daYVlrSnZNRmRDZDNCMWNsQlZTV0p1TUZodVpYcFdXU3R2TW01YVMxYzJObTVOWVhjMFJHVTNSa3A2UnpZNFRWZFJVSEYxWjFaaUwzZzVWRlkyTkZwaVJYaGxNbmxZVVVWblRHSmxUMkpYUlhKVmIxWldVRXh0Ym05d1VDOURUR3BTZFdRMlFVRjRkbTloZVZaSGF6RTNjRVpDWkZKTE5FRk5PRXhoYkZSWE5rRjFjbEJIT1RsV1ZsaDFRalIyYVUxbVVqZEdaM1ppZUVGRlNtc3ZUMHBzZHpGaVVsVklNR1l2VEc5b2F6bEVhalIzZEVwTk4wbzVZbkUwTWtGTllXRjNkRTlrVjJWQmFXZHRkSGxRUTAxR2N6UlBOSE5IVjNkSlVUVk1NVkpCVEhKTUwwMTZZemw2TUd4SWNHNXBTMUZaWlZGelFWVmxNMGxhUWpkR1ZqSjBNaXRvVjA1clNYRlBSbVJ5V1d3NVdXWlFNMUpwYm5KUWQydFlOV3hJVVZvMVVtSkdWRGhKV0hGVlFubHNhelZFZEhFclEwTnZTVmxuZG1sT01DOVhlR2t3VUVaeFIybGhORFl3TkVoMFIxSTFVRTQxSzBGUGRIWmxaVEZMUkdaV1dHNVpRazB6UTA5TGNpdHNZMkpSTm1oT1VqaEtURWhEZHpRMmRVNHlNRE5UYW5GMGVYcENlVU4yVUVzd1ZtNXRTRko1V0VsU04zcFplRXhLU1ZWc2VYZGhaRW8xTTNGaFJHVTBUbTV2TUdoWldITjRhVVJrY1djd0wzaERNVzlRU1hGMFNrUlljRVl3WlZobmVHWm9RM0p3TjFKS09GRXlWelJuTTA5SldXdG9UbTFFYlhaa01pOWxkMFpHUWxOdGVITlFUMVUxUVhWeVVXdFRlRzVMWVZJMFZ5OWtUMFpYUlZwbVZWcDBNM2RzVVVGUWJDc3pUVFpsZEd0dE1WZEpSek5aVFM5d2NUZExUR2s1UVdkMGNXZGxOMGwwZW1aM1dESm5WVmhtYjNOcWFWTkdVbU5XTTBWT1JFTlBLMkV2UWtGelNFODRVbU4zY0VsWlFVSkhVRVJ3U1d0cFVrOTJhM0ZxVG5KS1dYUXplVVF6ZFdwWFdYVjNiVkpsUWtseWRtaFdWbU5LZVNzNVFqZHVNRTFSZEU1d1JHOUpXRzlpUzBSNmVsbFlTRTF6YmpaSk1ucFJWbmhpSzNNMk1VUlBaR2t3VWtGbmQwSXpjREZPVWpWaWJUUkVNbkZpU25ORVNHRlpjWHBKVWpGVVVqQXJaVkVyYjFsMVIxVjVaM0pUTTJWUlZVNUZaSEoxWlcxclkxQktZbVpRSzNWVVJFa3ZWazlEUlc5bVRXcHVkVEJ4SzFNdmJUVmFkbXRUT0ZkSVZrbDRXSHByT1ZRMGIwVTNMM2R5WlhRMWRUbGFURFpITDNsaWRVbEVaR1oyTlVsUFYwWktVM3BaZVUwMlYzVlNVR05qZGtGNmVpdFNZWHBGTUdkWVVEVlhRbmxxUlZkUFMxQlFPSGN6VlM4M2RuaE1SellyTDNOd2NqbFVTRzVaWVdjMlEzRTFPVkpvV0M4MWRDOXVjVk0xWXpSNU1WbzFka3hUVVd0M2NUUTRWMFJXZG5GYVRtMUdNVzFKTmtodlFVZzRLMGxTWWpkbFZGTXJSRlJRV0daNE1VZGhZbVY1Y1ZSb1FWcGxaMGs1WVhRNGFUWTNLM1JvY2xsemNteHNURzF5Y3pOUFVUbGFhekJ0V2pnNWJ6ZFBOV1JKV2psTVJpODNWVlZrTVhoSmQybHdhVEJ1ZEhCYWVtNWpUMFpFUlZSbWIwSXZhU3RuYm1wVk1HOW5lRUpLWkM5MFZsZENhaXRzVUdKQmFuRkNTVWhUVlRsak1HeFNlWFpzU1VaRk0yWlNNVEpGV1c5UlVXaEVXR3N6TVd0VFJHVm1abFJ0VDFRd1lrNVFWelZzUjJoWFQzZEhOMVUwVmxSUVR5OVJWVkl4Y0hGbU5ETmhURkoxY0ZSRlpHRXJSbTl4VW00eVRFUlJabU5NT0M5UFNXUXhZMFV2T0ZKSWVsZDVlVXQxVTJkQ2MycHBNM1Y1UmpBMk0ydDBjMWxEYVVKS1ZIcG9TalJGU0ZaNmJXZ3JRVTVaVUhOcVprRjFOMFpsVUVJMFNEaFdhbVpTZUdkaFFVRlZTMHhhYzJobGJuaGxkMFpyZDNkQ1ZsUTNTa0UyYVRkNVRVaG5Sa0U0V1dwMU1EZDJkbkp4ZUdjclRubzViRkJHVnpWWGQyZHdVRFkzTUZvMVJDOTRUR3BoTkVaMFZYQnhjMGhTWTBoSU1XZzFhbmRhUnpOcVJqVkxhV3A1YURSVksyRm9WSEJTVGk5amRGZEdjVU5YTm14b1UxRXdiSEF3VWt0M2ExQjNNM2RFY2xkRVdrMTBjRVJ2VlZvNVYyZDZVWGxKVUhwWVZtdHJhbTVqWmpaUFZGSTFhMWhsSzJWT1MzcEJMM2Q2UjJWcVVXRkxWVmxTYnpkbVpEa3ZkRVJ2ZVc5R00wcE9XVk50TmpSTE5XNHpjbGM1Ymt0SFdGWm1jM1IyV0dkUlYzQm9hRWg2WVZGTldGVkhNMVpsTVM5QmRtNTZkbXBtTHpaNlppOXFWR1JWWm1wU2RVcHZhRkV4U2swM2JubHdXbUpKSzBKblpWWlpVR0pxVkVSMFltZGFLM3BxUXk5aVZXNTRNV1JYVWxVM2RtRkxka2xSYnpjd2VtMTJiMU5wWWtoWVNWSkVNVE00ZW1acVZXMXBZMDFCVGpoeWRVaElSa2RNWTNFd2JGUnhhRTV6YUZKb2NHcEJTV3gxTmxSUVduRXJUVk00YkVkVWN5OUpiR3BXVUdnM1JuSk1VRW8zWVdwb2FuVnNjRXgyYUV0WlpXNVhSSGgwVW1OemJtdHZRemx1ZGpSemJHTnpORmN5U0ZwUVNHcElPRmRrVmtaVFZuUnFRblpaV0dFNFJVdGFVMmR1YTBzd1MwcFBWMnhVZGxGc2QwbFFSVXBoWjJOMFYyVkNiMWN4UjFkMU9YUTFVVDA5UEM5RFpYSjBSVzV5YjJ4c1EyaGhiR3hsYm1kbFBqeFRhV2R1WlhKVWFIVnRZbkJ5YVc1MFBqbEVRME0xTURBM01FWkdRalU1UVRoQk5qQTFOa0V3UlRreU1EQTFSamhGT1VGQk9VTTNSakk4TDFOcFoyNWxjbFJvZFcxaWNISnBiblErUEM5RVlYUmhQanhUYVdkdVlYUjFjbVUrTXpBNE1qQXlNVEF3TmpBNU1rRTROalE0T0RaR056QkVNREV3TnpBeVFUQTRNakF5TURFek1EZ3lNREZHUkRBeU1ERXdNVE14TUVZek1EQkVNRFl3T1RZd09EWTBPREF4TmpVd016QTBNREl3TVRBMU1EQXpNREJDTURZd09USkJPRFkwT0RnMlJqY3dSREF4TURjd01UTXhPREl3TVVRNE16QTRNakF4UkRRd01qQXhNREV6TURneFFVTXpNRGd4T1Rjek1UQkNNekF3T1RBMk1ETTFOVEEwTURZeE16QXlOVFUxTXpNeE1UTXpNREV4TURZd016VTFNRFF3T0RFek1FRTFOell4TnpNMk9EWTVOa1UyTnpjME5rWTJSVE14TVRBek1EQkZNRFl3TXpVMU1EUXdOekV6TURjMU1qWTFOalEyUkRaR05rVTJORE14TVRJek1ERXdNRFl3TXpVMU1EUXdRVEV6TURrMFJEWTVOak0zTWpaR056TTJSalkyTnpRek1UQkRNekF3UVRBMk1ETTFOVEEwTUVJeE16QXpORFUwUkRVd016RXpSak13TTBRd05qQXpOVFV3TkRBek1UTXpOalV6TkRNMFJqWkZOa00yT1RaRk5qVTBOell4TnpRMk5UYzNOakUzT1RVek5qazJOelpGTmprMlJUWTNORE0yTlRjeU56UTJPVFkyTmprMk16WXhOelEyTlRKRk5rUTJNVFpGTmpFMk56WTFNa1UyUkRZNU5qTTNNalpHTnpNMlJqWTJOelF5UlRZek5rWTJSREF5TVRBeE1rSXdOamN4TUVNMlFqWTBOemcyUWtNNVF6WkRRalkyUmprMU9UTkVRVE13TUVRd05qQTVOakE0TmpRNE1ERTJOVEF6TURRd01qQXhNRFV3TURNd01FUXdOakE1TWtFNE5qUTRPRFpHTnpCRU1ERXdNVEF4TURVd01EQTBPREl3TVRBd09VTkVOMFF6TXpKRE0wTTBOVEZFTTBSQ01qUXpOemN3TWtJNE1rUTNSRFpEUlRReU5UVkNOa1k0TURoRU4wSXlRalJETTBNM05qaEdOemcwTkRZM1JrSTBNRU0xUmpreE1EVkRSRGc0TlRWR1FURkdOelk0TnpReVJqRkVOa0k0TnpVMVJqZEROelExUmtSQk5rTXlOMFkxTVVJd1JVUXhNRGhFUVRVME5EWkZOMEUxUmtFeVJVWXhSVEpCUVRCQ01FSkROa1k0T1RZNE5rWTFRek5EUWtKR1EwVXhRalUxTmpVeU16VTJNVUl3TlRBMU5rSXhNRFkzTmprM05VTTFOVU0zUVVNM1JETkZSRFF4TmpJek5UZ3hPRUUwTmpkQ05rVkdNRU5ET1VZNE5FTXdOa1k1TkRNMU1EazROamsxTmpWRFJrTkJOVU15UWtRME5rVTVOVFZEUkRFME16QTVNemM0TUVSR1FVTTFRemM0T1RNNE5VTTNNVE01TWtKRE1VSkdOa1JHUWprME1UWTFOalZCUkRORk1qRTVNVFZHTmtJMk9URkRPVFl5TkRZeE5VWXpSakF5UVRrM1FrRkJOelV3TWtRd05EZzRORGd5TmtZMk9ERTVSVFJDTWpKQ05UZ3pOak13T1RWQk9URTNNakpHTUVSR1JUTkROVGRGTmtKRk9FRkdNelkxUWpFelJERTNPRU14UkRRM01qZzBRVVk0UVVaR05UUTRNMFV4UlRnek9UZzBSREE1UXpZd1JESkNRemxFUlRJMk9USTJSRGt4T1RnM1EwWTFOVVZFUWpjM1FUWTNRak14UWtNeU5Ea3dRamc0TTBJeE9UWkNOREZFTkVNMVJqUkVNREEzUXpNeE5EVkROelU0UmpVM01qazBPREl6UVVZOEwxTnBaMjVoZEhWeVpUNDhMME5sY25SRmJuSnZiR3hVYjJ0bGJqND0wDQYJKoZIhvcNAQELBQADggEBAG76Rg6Wtx9Q2ygxBaKYzqVnxLAm9dqfMJ07jhvvtGXhofedoivHp9+EOfwpwctHeJ2Bs3yNuhbns/QBf+11rL3M7bxdqptX6X+aK5UScKVjpcpHyHypWLsIXaSunZDAKRPUZXMkj/dvUQRLBRgvJOuRpbwFy3rZWXeL8ciOjBzgnO8jVN9yMukZmiC3Zj8XRnil8mWGkNHg9+SHgPsS4RFZRmaHvmg4X8bUtm0Hx2640vLF4iIBaO6CjYGHNkM+/gkOt/Hm2fMpYZfDUy1Dp0ekHjl5KKQMbL49a6dxEZ2mdzwc+35wYEynToXgeoegZiyfX30ArSbmGPK+VGvjuh4="

    def "test getCertificatesFromJKS"(){
        when:
        def certs = CertUtils.getCertificatesFromJKS("src/test/resources/scepreceiver.jks")

        then:
        certs != null
        certs.size() == 3
    }

    def "test methods to get subject alternative name"(){
        when:
        PKCS10CertificationRequest request = CertUtils.getPKCS10CertificationRequestFromFile("src/test/resources/samplerequest.csr")

        Extension sanExtension = CertUtils.getSubjectAlternativeNameExtensionFromCertificateRequest(request)
        String upn = CertUtils.getSubjectAlternativeNameFieldFromExtension(sanExtension, GeneralName.otherName)
        String email =  CertUtils.getSubjectAlternativeNameFieldFromExtension(sanExtension, GeneralName.rfc822Name)

        println "upn = " + upn
        println "email = " + email
        then:
        request != null

    }
}