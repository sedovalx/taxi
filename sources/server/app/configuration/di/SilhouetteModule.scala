package configuration.di

import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.util.BCryptPasswordHasher
import scaldi.Module

/**
 * DI контейнер для auth
 * @see http://scaldi.org/learn/#overview
 *      http://silhouette.mohiva.com/v2.0/docs/introduction
 */
class SilhouetteModule extends Module {
  // The default password hasher implementation.
  bind [PasswordHasher] to new BCryptPasswordHasher
}
