# Bonjour

This repository compiles the native Apple Bonjour code (dnssd.dll) and merges it into the Bonjour Java library. The Java library additionally provides itself with the jdns_sd.dll (contains the required JNI code) and therefore presents itselft as an all-in-one package to use Bonjour in a Java environment. The only necessary step to use it, is to pass a directory to the DllProvider where it can extract its dll files into.

# Source

The code is based on version 878.260.1 (https://opensource.apple.com/tarballs/mDNSResponder/) and includes a small fix in the native code. It corrects the shutdown behaviour of Bonjour services which lead to unclosed sockets. Java (or rather Windows) passes down handles to child processes - this cannot be prevented, at least not deterministically (see e.g. https://stackoverflow.com/questions/11847793/are-tcp-socket-handles-inheritable). This leads to unclosed handles while a child process in Java is active, which in turn leads to Bonjour services not being stopped since the WSAEvent "FD_CLOSE" never gets sent (which is necessary for the final deregistering of a service).
To fix this, we changed the "stop" behaviour of Bonjour by adding a shutdown() before closesocket() as documented here: https://docs.microsoft.com/en-us/windows/win32/api/winsock/nf-winsock-shutdown#remarks

