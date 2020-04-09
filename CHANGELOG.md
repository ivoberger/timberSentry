# Changelog

## 0.4
- Migration to Sentry 2 API
- Update build dependencies

### BC break:
- Recommended Sentry initialization is over `<meta-data>` in `Manifest.xml`, see https://docs.sentry.io/platforms/android/#connecting-the-sdk-to-sentry
  - Initialization parameters `context`, `sentryDsn` are removed
- Extras are placed to captured event all the time
  - Initialization parameter `addAllExtrasToContext` is removed
